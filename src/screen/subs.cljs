(ns screen.subs
  (:require [re-frame.core :refer [reg-sub]]
            [hangman.rules :as rules]))

(defn leader [db]
  (get-in db (:leader db)))

(defn current-player [db]
  (get-in db (:current-player db)))

(reg-sub :guesses :guesses)
(reg-sub :word :word)
(reg-sub :score :score)
(reg-sub :failed-guesses rules/failed-guesses)
(reg-sub :game-state :game-state)
(reg-sub :players #(-> % :players/by-device-id vals))
(reg-sub :leader #(get-in % (:leader %)))
(reg-sub :current-player #(or (current-player %) (leader %)))
