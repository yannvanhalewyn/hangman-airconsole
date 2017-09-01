(ns hangman.rules
  (:require [clojure.string :as str]))

(def ALLOWED-MISSES 8)

(defn gen-new-game!
  "Returns a new game db for given word"
  [word]
  {:guesses #{} :word word})

(defn correct-guesses
  "Given a word and a set of guesses, returns the set of guesses that
  match a letter in the target word."
  [{:keys [word guesses]}]
  (filter (set (str/lower-case word)) guesses))

(defn failed-guesses
  "Given a word and a set of guesses, returns the set of guesses that
  don't match any letter in the target word."
  [{:keys [word guesses]}]
  (filter (complement (set (str/lower-case word))) guesses))

(defn game-state
  "Given a game and some guesses, returns the state in which the game
  is. The state can be one of `#{:guessing :lost :won}`"
  [{:keys [word guesses] :as game}]
  (let [correct-count (count (correct-guesses game))
        failed-count (count (failed-guesses game))]
    (cond
      (>= failed-count ALLOWED-MISSES) :lost
      (every? guesses (set (str/lower-case word))) :won
      :else :guessing)))
