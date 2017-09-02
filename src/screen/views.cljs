(ns screen.views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [clojure.string :as str]
            [hangman.views.hangman :as hangman]))

(defn target-word []
  (let [guesses (subscribe [:guesses])
        word (subscribe [:word])
        game-state (subscribe [:game-state])]
    [:div.u-centered.u-extra-large.u-margin-top-l
     (doall
      (map-indexed
       (fn [i char]
         ^{:key i}
         [:span {:style {:width "0.9em" :display "inline-block"}}
          (if (or (@guesses (str/lower-case char)) (not= :guessing @game-state))
            char
            "_")])
       @word))]))

(defn failed-guesses []
  (let [failures (subscribe [:failed-guesses])]
    (if-not (empty? @failures)
      [:div.u-margin-top-l.u-large
       (map-indexed
        (fn [i char]
          ^{:key i}
          [:span.failure.u-margin-right.u-strikethrough char])
        @failures)])))

(defn guessing []
  [:div.u-centered
   [hangman/component {:step (count @(subscribe [:failed-guesses]))
                       :size 250}]
   [target-word]
   [failed-guesses]])

(defn players []
  (let [players @(subscribe [:players])]
    [:div
     (for [{:keys [thumb name device-id score]} players]
       ^{:key device-id}
       [:div.u-margin-top
        [:div.l-row.u-centered
         [:img.player__thumb.l-col-6 {:src thumb}]
         [:div.l-col-3.u-margin-top-l
          [:div.u-large.muted name]
          [:div.u-margin-top.u-extra-large (or score 0)]]]])]))

(defn word-select []
  (let [{:keys [name]} @(subscribe [:leader])]
    [:h2.muted name " can select a word."]))

(defn game []
  [:div.l-content
   [:h1.u-extra-large.u-margin-top-l.u-muted "Hangman Battle"]
   [:div.l-row
    [:div.l-col-6 [players]]
    [:div.l-col-6
     (let [game-state @(subscribe [:game-state])]
       (case game-state
         :word-select [word-select]
         :guessing [guessing]
         (:won :lost) [guessing]
         [:h1 "No such game-state " game-state]))]]])
