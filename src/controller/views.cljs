(ns controller.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [clojure.string :as str]
            [screen.game :as game]))

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

(def ALPHABET "abcdefghijklmnopqrstuvwxyz")

(defn alphabet [{:keys [word guesses on-select]}]
  [:div.u-flex.u-flex-center
   (for [letter ALPHABET]
     (let [used? (guesses letter)]
       ^{:key letter}
       [:button.u-large.guess-key.u-margin-top
        {:on-click #(on-select letter)
         :disabled used?
         :class (if used?
                  (if ((set word) letter)
                    "guess-key--green"
                    "guess-key--red"))}
        (str/upper-case letter)]))])

(defn guessing []
  (let [leader? @(re-frame/subscribe [:leader?])
        word @(re-frame/subscribe [:word])
        guesses @(re-frame/subscribe [:guesses])]
    (if leader?
      [:h1 "Waiting"]
      [:div
       [:h1.u-centered.u-extra-large "Guess!"]
       [alphabet {:word word
                  :guesses guesses
                  :on-select #(re-frame/dispatch [:submit-guess %])}]])))

(defn round-end []
  [:div
   [:h1 "Game done!"]
   [:button.btn {:on-click #(re-frame/dispatch [:request-new-game])} "New game"]])

(defn app []
  (let [state @(re-frame/subscribe [:game-state])]
    (case state
      :word-select [word-select]
      :guessing [guessing]
      (:won :lost) [round-end]
      [:h1.u-extra-large "No such state " state])))
