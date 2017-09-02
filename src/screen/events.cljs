(ns screen.events
  (:require [re-frame.core :as rf]
            [hangman.rules :as rules]
            [air-console.core :as ac]))

;; Coeffects
;; =========

(rf/reg-fx :broadcast ac/broadcast)

;; Handlers
;; ========

(def NEW_GAME {:game-state :word-select
               :guesses #{}
               :word nil})

(defn- init
  "Given a random-word and local-storage data, returns an initial db
  to work with."
  [_]
  NEW_GAME)

(defn- new-game
  "Given an old db and a random word, sets up a db for a new game"
  [{:keys [db]} _]
  {:db (merge db NEW_GAME)
   :broadcast [:game-state :word-select]})

(defn- guess
  "Given a db and a guess, consult the rules and progress the
  gameplay. Returns a new app-db."
  [{:keys [db]} [_ guess]]
  (let [[new-db broadcast]
        (let [new-db (as-> db $
                       (update $ :guesses conj guess)
                       (assoc $ :game-state (rules/game-state $)))]
          (case [(:game-state db) (:game-state new-db)]

            ;; Word was guessed
            [:guessing :won]
            [(update-in new-db (conj (:current-player new-db) :score) inc)
             [:game-state :round-end]]

            ;; Failed to guess word
            [:guessing :lost]
            [(update-in new-db (conj (:leader new-db) :score) inc)
             [:game-state :round-end]]

            ;; Game not over yet
            [:guessing :guessing] [new-db]
            [db]))]
    (-> {:db new-db} (conj (when broadcast [:broadcast broadcast])))))

(defn- add-player
  "Adds a player to the game"
  [{:keys [db]} [_ device-id player-id]]
  {:db (assoc-in db [:players/by-device-id device-id]
                 {:device-id device-id
                  :player-id player-id
                  :score 0})
   :dispatch [:ensure-leader]})

(defn- remove-player
  "Removes a player from the game"
  [{:keys [db]} [_ device-id]]
  {:db (update db :players/by-device-id dissoc device-id)
   :dispatch [:ensure-leader]})

(defn- ensure-leader
  "Ensures a leader is present in the party"
  [{:keys [db]}]
  (let [new-leader (or (:leader db)
                       [:players/by-device-id
                        (-> db :players/by-device-id first key)])]
    {:db (assoc db :leader new-leader)
     :broadcast [:new-leader (second new-leader)]}))

(defn- first-available-player-id [db]
  (let [leader-id (second (:leader db))]
    (-> db :players/by-device-id (dissoc leader-id) first key)))

(defn- submit-word
  "Sets the target word of the game"
  [{:keys [db]} [_ word]]
  {:db (assoc db
         :word word
         :game-state :guessing
         :current-player [:players/by-device-id (first-available-player-id db)])
   :broadcast [:game-state :guessing]})

;; Register handlers
;; =================

(rf/reg-event-db :initialize-db init)
(rf/reg-event-fx :request-new-game new-game)
(rf/reg-event-fx :guess guess)
(rf/reg-event-fx :player-joined add-player)
(rf/reg-event-fx :player-left remove-player)
(rf/reg-event-fx :ensure-leader ensure-leader)
(rf/reg-event-fx :submit-word submit-word)
