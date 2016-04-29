# heartbeat

Expose a heartbeat route for Ring web apps. This non-canonical version uses http-kit in place of clj-http.

## Install

In your project.clj:

````clojure
[crossref/heartbeat "0.1.4"]
````

## Usage

Define some web and service checks:

````clojure
(use 'heartbeat.core)

(def-web-check :google "http://www.google.com")
(def-web-check :crossref-api "http://api.crossref.org/works")

(def-service-check :mongo (fn [] (congomongo.core/fetch "dois")))
````

Expose the /heartbeat route using the provided ring middleware:

````clojure
(use 'heartbeat.ring)

(-> my-api-routes
	(wrap-heartbeat))
````
