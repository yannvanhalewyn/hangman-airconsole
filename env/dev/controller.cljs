(ns dev.controller
  (:require [controller.core :as controller]))

(.log js/console "USER NS controller")

(defonce on-js-load (do (controller/init!) controller/mount-root))
