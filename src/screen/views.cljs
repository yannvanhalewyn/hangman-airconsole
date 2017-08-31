(ns screen.views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [clojure.string :as str]
            [hangman.views.hangman :as hangman]))

(defn- new-game-btn [msg]
  [:button.btn {:on-click #(dispatch [:new-game])} msg])

(defn status []
  (let [game-state (subscribe [:game-state])]
    (fn []
      [:div.u-bold.u-large.u-centered
       (case @game-state
         :lost [:div.failure "You lost." [new-game-btn "Try again.."]]
         :won [:div.success "You won!" [new-game-btn "New game!"]]
         "Please enter a guess..")])))

(defn score []
  (let [score (subscribe [:score])]
    (fn []
      [:div.u-centered.u-margin-top
       (if-let [wins (:wins @score)]
         [:span.success.u-margin-right-l "Won: " wins])
       (if-let [losses (:losses @score)]
         [:span.failure "Lost: " losses])])))

(defn target-word []
  (let [guesses (subscribe [:guesses])
        word (subscribe [:word])
        game-state (subscribe [:game-state])]
    (fn []
      [:div.u-centered.u-large
       (doall
        (map-indexed
         (fn [i char]
           ^{:key i}
           [:span.u-margin-right
            (if (or (@guesses (str/lower-case char)) (not= :playing @game-state))
              char
              "_")])
         @word))])))

(defn failed-guesses []
  (let [failures (subscribe [:failed-guesses])]
    (fn []
      (if-not (empty? @failures)
        [:div.u-centered.u-margin-top
         "Failed: "
         (map-indexed
          (fn [i char]
            ^{:key i}
            [:span.failure.u-margin-right.u-strikethrough char])
          @failures)]))))

(defn game []
  [:div
   [:h1.u-centered.u-extra-large "HANGMAN"]
   [status]
   [score]
   [hangman/component]
   [target-word]
   [failed-guesses]])
