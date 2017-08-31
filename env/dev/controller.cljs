(ns dev.controller
  (:require [controller.core :as controller]))

(defonce on-js-load (do (controller/init!) controller/mount-root))
