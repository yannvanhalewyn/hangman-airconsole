(ns screen.core
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [air-console.core :as ac]
            [screen.subs]
            [screen.events]
            [screen.views :as views]))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/game] (.getElementById js/document "screen")))

(defn init! []
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))

;; Networking
;; ==========

(defn on-connect [device-id]
  (.log js/console "Connect")
  (ac/set-active-players 2))

(defn on-disconnect [device-id]
  (ac/set-active-players 0))

(defn on-message [device-id event]
  (re-frame/dispatch event))

(defonce foo
  (do
    (ac/on-connect! on-connect)
    (ac/on-disconnect! on-disconnect)
    (ac/on-message! on-message)))
