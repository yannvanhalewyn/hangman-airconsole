(ns controller.core
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [air-console.core :as ac]
            [controller.views :as views]))

;; Effects
;; =======

(def INITIAL_STATE {:game-state :word-select
                    :leader? false})

(re-frame/reg-fx :message #(ac/message ac/SCREEN %))
(re-frame/reg-cofx :device-id #(assoc % :device-id (ac/device-id)))

;; Subs
;; ====

(re-frame/reg-sub :game-state :game-state)
(re-frame/reg-sub :leader? :leader?)
(re-frame/reg-sub :word :word)
(re-frame/reg-sub :guesses :guesses)


;; Handlers
;; ========

(re-frame/reg-event-db
 :initialize-db
 (fn [db] INITIAL_STATE))

(re-frame/reg-event-fx
 :submit-guess
 (fn [cofx [_ guess]]
   {:message [:guess guess]}))

(re-frame/reg-event-fx
 :submit-word
 (fn [_ [_ word]]
   {:message [:submit-word word]}))

(re-frame/reg-event-fx
 :new-leader
 (re-frame/inject-cofx :device-id)
 (fn [{:keys [db device-id]} [_ leader-id]]
   {:db {:game-state :word-select
         :leader? (= device-id leader-id)}}))

(re-frame/reg-event-fx
 :request-new-game
 (fn [_ _]
   {:message [:request-new-game]}))

(re-frame/reg-event-fx
 :sync
 (re-frame/inject-cofx :device-id)
 (fn [{:keys [db device-id]} [_ new-db]]
   {:db
    (-> (merge db new-db)
        (assoc :leader? (= (second (:leader new-db)) device-id)))}))

(re-frame/reg-event-db
 :round-end
 (fn [db] (assoc db :game-state :end)))

;; Startup
;; =======

(defn- on-message [device-id msg]
  (.log js/console "Controller " (ac/device-id) " got " msg)
  (re-frame/dispatch msg))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/app]
    (.getElementById js/document "controller")))

(defn init! []
  (ac/init! {:orientation ac/ORIENTATION_LANDSCAPE})
  (re-frame/dispatch-sync [:initialize-db])
  (ac/on-message! on-message)
  (mount-root))
