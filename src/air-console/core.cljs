(ns air-console.core
  (:require [cljs.reader :refer [read-string]]))


(def ORIENTATION_LANDSCAPE (.-ORIENTATION_LANDSCAPE js/AirConsole))
(def SCREEN (.-SCREEN js/AirConsole))

(defonce AC (atom nil))

(defn init!
  ([] (init! {}))
  ([opts] (reset! AC (js/AirConsole. (clj->js opts)))))

;; Handler Setters
;; ===============

(defn on-connect! [connect-fn]
  (set! (.-onConnect @AC) connect-fn))

(defn on-disconnect! [connect-fn]
  (set! (.-onDisconnect @AC) connect-fn))

(defn on-message! [msg-fn]
  (set! (.-onMessage @AC)
        (fn [device-id data]
          (msg-fn device-id (read-string data)))))

;; Getters
;; =======

(defn device-id [] (.getDeviceId @AC))

;; Commands
;; ========

(defn set-active-players [players]
  (.setActivePlayers @AC players))

(defn message [device-id data]
  (.message @AC device-id (pr-str data)))

(defn broadcast [data]
  (.broadcast @AC (pr-str data)))

;; Utils
;; =====

(defn device->player [device-id]
  (.convertDeviceIdToPlayerNumber @AC device-id))

(defn device->nickname [device-id]
  (.getNickname @AC device-id))

(defn device->profile-picture [device-id]
  (.getProfilePicture @AC device-id))

(defn player->device [device-id]
  (.convertPlayerNumberToDeviceId @AC device-id))
