(ns screen.events
  (:require [re-frame.core :as rf]
            [hangman.rules :as rules]))

;; Handlers
;; ========

(defn- init
  "Given a random-word and local-storage data, returns an initial db
  to work with."
  [_]
  (rules/gen-new-game! "Elephant"))

(defn- new-game
  "Given an old db and a random word, sets up a db for a new game"
  [db]
  (merge db (rules/gen-new-game! "Elephant")))

(defn- guess
  "Given a db and a guess, consult the rules and progress the
  gameplay. Returns a new app-db."
  [db [_ guess]]
  (let [new-db (update db :guesses conj guess)]
    (case [(rules/game-state db) (rules/game-state new-db)]
      [:playing :won] (update-in new-db [:score :wins] inc)
      [:playing :lost] (update-in new-db [:score :losses] inc)
      [:playing :playing] new-db
      db)))

;; Register handlers
;; =================

(rf/reg-event-db :initialize-db init)
(rf/reg-event-db :new-game new-game)
(rf/reg-event-db :guess guess)
