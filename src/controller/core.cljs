(ns controller.core
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [air-console.core :as ac]))

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

;; Views
;; =====

(defn word-input []
  (let [word (reagent/atom "")]
    (fn []
      [:div
       [:h1.u-centered.u-extra-large "Select Word"]
       [:form
        {:on-submit (fn [e]
                      (.preventDefault e)
                      (re-frame/dispatch [:submit-word @word]))}
        [:input {:type :text
                 :value @word
                 :on-change #(reset! word (.. % -target -value))}]]])))

(defn word-select []
  (let [leader? (re-frame/subscribe [:leader?])]
    (fn []
      (if @leader? word-input [:h1 "Waiting"]))))

(defn guessing []
  (let [leader? (re-frame/subscribe [:leader?])]
    (fn []
      (if @leader?
        [:h1 "Waiting"]
        [:div
         [:h1.u-centered.u-extra-large "Guess!"]
         [:input {:type :text
                  :value ""
                  :on-change #(re-frame/dispatch [:submit-guess (.. % -target -value)])}]]))))

(defn app []
  (let [state (re-frame/subscribe [:game-state])]
    (fn []
      (case @state
        :word-select word-select
        :guessing guessing
        :end [:h1 "Game done!"]
        [:h1.u-extra-large "No such state " @state]))))

;; Handlers
;; ========

(re-frame/reg-event-db
 :initialize-db
 (fn [db [_ device-id]]
   (assoc INITIAL_STATE :device-id device-id)))

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

(re-frame/reg-event-db
 :word-submitted
 (fn [db]
   (assoc db :game-state :guessing)))

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
  (reagent/render [app]
    (.getElementById js/document "controller")))

(defn init! []
  (re-frame/dispatch-sync [:initialize-db (ac/device-id)])
  (ac/on-message! on-message)
  (mount-root))
