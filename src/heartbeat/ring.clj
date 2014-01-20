(ns heartbeat.ring
  (:require [clojure.string :as string]
            [heartbeat.core :refer [heartbeat]]
            [clojure.data.json :as json]
            [ring.util.response :refer [response status header]]))

(def heartbeat-path "/heartbeat")

(defn wrap-heartbeat
  [handler]
  (fn [request]
    (if (.startsWith (:uri request) heartbeat-path)
      (-> (heartbeat)
          (json/write-str)
          (response)
          (status 200)
          (header "Content-Type" "application/json"))
      (handler request))))
  
