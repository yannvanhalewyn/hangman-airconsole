(ns screen.core
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [air-console.core :as ac]
            [screen.subs]
            [screen.events]
            [screen.views :as views]))

;; Networking
;; ==========

(defn on-connect [device-id]
  (ac/set-active-players 4)
  (re-frame/dispatch
   [:player-joined {:device-id device-id
                    :player-id (ac/device->player device-id)
                    :name (ac/device->nickname device-id)
                    :thumb (ac/device->profile-picture device-id)}]))

(defn on-disconnect [device-id]
  (re-frame/dispatch [:player-left device-id]))

(defn on-message [device-id event]
  (.log js/console "Screen got " device-id event)
  (re-frame/dispatch event))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/game] (.getElementById js/document "screen")))

(defn init! []
  (ac/init!)
  (re-frame/dispatch-sync [:initialize-db])
  (ac/on-connect! on-connect)
  (ac/on-disconnect! on-disconnect)
  (ac/on-message! on-message)
  (mount-root))
