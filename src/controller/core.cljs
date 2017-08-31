(ns controller.core
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]))

(re-frame/reg-event-db
 :initialize-db
 (fn [] {}))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [:div "hi"] (.getElementById js/document "controller")))

(defn init! []
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))
