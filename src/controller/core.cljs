(ns controller.core
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [air-console.core :as ac]))

;; Effects
;; =======

(re-frame/reg-event-db
 :initialize-db
 (fn [] {}))

(re-frame/reg-fx :message #(ac/message ac/SCREEN %))

;; Handlers
;; ========

(re-frame/reg-event-fx
 :submit-guess
 (fn [cofx [_ guess]]
   {:message [:guess guess]}))

(defn view []
  [:div
   [:h1.u-centered.u-extra-large "Hangman"]
   [:input {:type :text
            :value ""
            :on-change #(re-frame/dispatch [:submit-guess (.. % -target -value)])}]])

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [view]
    (.getElementById js/document "controller")))

(defn init! []
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))

(defn- on-message [device msg]
  (.log js/console "Controller received" msg))

(defonce _ (ac/on-message! on-message))
