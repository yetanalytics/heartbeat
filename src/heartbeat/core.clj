(ns heartbeat.core
  (:require [clj-http.client :as http]))

(def heartbeat-hooks (atom {}))

(defn def-web-check
  "Define a web API dependency. A call will be made to URL each time
   the heartbeat path is requested, which will report on the response's
   status code."
  [name url]
  (swap! heartbeat-hooks assoc-in [:web name] url))
  
(defn def-service-check
  "Define a service dependency. A call to check-fn will be made each
   time the heartbeat path is requested. The check-fn should return
   a value if the service check succeeds, or nil otherwise."
  [name check-fn]
  (swap! heartbeat-hooks assoc-in [:service name] check-fn))

(defn def-version
  "Set the version reported by heartbeat."
  [version]
  (swap! heartbeat-hooks assoc-in [:version] version))

(defn check-web [[name url]]
  {:name name
   :status (try
             (let [response (http/get url)]
               (if (or (http/success? response) 
                       (http/redirect? response))
                 :up
                 :down))
             (catch Exception e :down))})

(defn check-service [[name check-fn]]
  {:name name
   :status (try
             (if (check-fn) :up :down)
             (catch Exception e :down))})

(defn heartbeat []
  (let [web (map check-web (:web @heartbeat-hooks))
        service (map check-service (:service @heartbeat-hooks))]
    {:web web
     :service service
     :version (-> heartbeat-hooks deref :version)
     :hostname (.getCanonicalHostName (java.net.InetAddress/getLocalHost))
     :status (if (-> (filter #(= (:status %) :down) (concat web service))
                     (count)
                     (zero?))
               :up
               :down)}))

