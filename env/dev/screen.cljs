(ns dev.screen
  (:require [screen.core :as screen]))

(defonce on-js-load (do (screen/init!) screen/mount-root))
