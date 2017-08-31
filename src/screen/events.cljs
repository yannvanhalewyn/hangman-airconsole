(ns screen.events
  (:require [re-frame.core :as rf]
            [hangman.rules :as rules]
            [air-console.core :as ac]))

;; Coeffects
;; =========

(rf/reg-fx :ac-message #(apply ac/message %))

;; Handlers
;; ========

(def NEW_GAME {:game-state :waiting-for-word})

(defn- init
  "Given a random-word and local-storage data, returns an initial db
  to work with."
  [_]
  (merge NEW_GAME (rules/gen-new-game! "Elephant")))

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

(defn- add-player
  "Adds a player to the game"
  [{:keys [db]} [_ device-id]]
  {:db (assoc-in db [:players/by-device-id device-id] {:device-id device-id})
   :dispatch [:ensure-leader]})

(defn- remove-player
  "Removes a player from the game"
  [{:keys [db]} [_ device-id]]
  {:db (update db :players/by-device-id dissoc device-id)
   :dispatch [:ensure-leader]})

(defn- ensure-leader
  "Ensures a leader is present in the party"
  [{:keys [db]}]
  (if (get-in db (:leader db))
    {}
    (let [new-leader [:players/by-device-id
                      (-> db :players/by-device-id first key)]]
      {:db (assoc db :leader new-leader)
       :ac-message [(second new-leader) [:make-leader]]})) )

;; Register handlers
;; =================

(rf/reg-event-db :initialize-db init)
(rf/reg-event-db :new-game new-game)
(rf/reg-event-db :guess guess)
(rf/reg-event-fx :player-joined add-player)
(rf/reg-event-fx :player-left remove-player)
(rf/reg-event-fx :ensure-leader ensure-leader)
