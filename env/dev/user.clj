(ns user
  (:require [figwheel-sidecar.repl-api :as ra]))

(defn start []
  (ra/start-figwheel! "screen" "controller"))

(defn cljs-repl [build-id]
  (ra/cljs-repl build-id))

(defn stop []
  (ra/stop-figwheel!))
