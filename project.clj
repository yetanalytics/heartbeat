(defproject crossref/heartbeat "0.1.3"
  :description "Expose a heartbeat route for Ring web apps."
  :url "http://www.github.com/CrossRef/heartbeat"
  :signing {:gpg-key "labs@crossref.org"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.json "0.2.0"]
                 [clj-http "0.7.2"]
                 [ring "1.1.0"]])
