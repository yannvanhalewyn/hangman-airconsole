(ns controller.core
  (:require [air-console.core :as ac]))

(defn ^:export up []
  (ac/message [:move/up 40]))

(defn ^:export down []
  (ac/message [:move/down 40]))
