(ns screen.subs
  (:require [re-frame.core :refer [reg-sub]]
            [hangman.rules :as rules]))

(reg-sub :guesses :guesses)
(reg-sub :word :word)
(reg-sub :score :score)
(reg-sub :failed-guesses rules/failed-guesses)
(reg-sub :game-state :game-state)
