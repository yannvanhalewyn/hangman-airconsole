(ns dev.screen
  (:require [screen.core :as screen]))

(.log js/console "USER NS SCREEN")
(defonce on-js-load (do (screen/init!) screen/mount-root))
