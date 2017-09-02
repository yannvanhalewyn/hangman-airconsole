(ns screen.views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [clojure.string :as str]
            [hangman.views.hangman :as hangman]))

(defn status []
  (let [game-state (subscribe [:game-state])]
    [:div.u-bold.u-large.u-centered
     (case @game-state
       :lost [:div.failure "You lost."]
       :won [:div.success "You won!"]
       "Please enter a guess..")]))

(defn score []
  (let [score (subscribe [:score])]
    [:div.u-centered.u-margin-top
     (if-let [wins (:wins @score)]
       [:span.success.u-margin-right-l "Won: " wins])
     (if-let [losses (:losses @score)]
       [:span.failure "Lost: " losses])]))

(defn target-word []
  (let [guesses (subscribe [:guesses])
        word (subscribe [:word])
        game-state (subscribe [:game-state])]
    [:div.u-centered.u-large
     (doall
      (map-indexed
       (fn [i char]
         ^{:key i}
         [:span.u-margin-right
          (if (or (@guesses (str/lower-case char)) (not= :guessing @game-state))
            char
            "_")])
       @word))]))

(defn failed-guesses []
  (let [failures (subscribe [:failed-guesses])]
    (if-not (empty? @failures)
      [:div.u-centered.u-margin-top
       "Failed: "
       (map-indexed
        (fn [i char]
          ^{:key i}
          [:span.failure.u-margin-right.u-strikethrough char])
        @failures)])))

(defn guessing []
  [:div
   [:h1.u-centered.u-extra-large "HANGMAN"]
   [status]
   [score]
   [hangman/component]
   [target-word]
   [failed-guesses]])

(defn players []
  (let [players @(subscribe [:players])]
    [:div
     (for [{:keys [player-id device-id score]} players]
       ^{:key device-id}
       [:div
        [:span.u-bold.u-margin-right "Player: " (inc player-id)]
        [:span.u-bold.u-margin-right "Device: " device-id]
        [:span.u-bold.u-margin-right "Score: " score]])]))

(defn game []
  [:div
   [players]
   (let [game-state (subscribe [:game-state])]
     (case @game-state
       :word-select [:h1 "Waiting for word"]
       :guessing [guessing]
       :won [guessing]
       [:h1 "No such game-state " @game-state]))])
