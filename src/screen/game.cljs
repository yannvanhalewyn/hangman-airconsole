(ns screen.game
  (:require [hangman.rules :as rules]))

(defn- guess-type [{:keys [word guesses]} guess]
  (cond
    (guesses guess) :redundant
    ((set word) guess) :good
    :else :bad))

(defn- inc-score [game player]
  (update-in game (conj player :score) inc))

(defn- switch-leader
  "Returns a new game with a cycled leader"
  [game]
  (let [leader-id (second (:leader game))
        available-players (-> game :players/by-device-id (dissoc leader-id) keys)
        new-leader (or (first (filter #(> % leader-id) available-players))
                       (first available-players))]
    (assoc-in game [:leader 1] new-leader)))

(defn first-available-player-id [db]
  (let [leader-id (second (:leader db))]
    (-> db :players/by-device-id (dissoc leader-id) first key)))


(defn guess [game guess]
  (let [guess-success (guess-type game guess)
        new-game (as-> game $
                   (update $ :guesses conj guess)
                   (assoc $ :game-state (rules/game-state $)))]
    (case [(:game-state game) (:game-state new-game)]

      ;; Word was guessed
      [:guessing :won] (inc-score new-game (:current-player new-game))

      ;; Failed to guess word
      [:guessing :lost] (inc-score new-game (:leader new-game))

      ;; Game not over yet
      [:guessing :guessing] new-game
      game)))
