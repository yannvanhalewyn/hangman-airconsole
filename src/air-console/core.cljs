(ns air-console.core
  (:require [cljs.reader :refer [read-string]]))

(defonce AC (js/AirConsole.))

(def SCREEN (.-SCREEN js/AirConsole))

;; Handler Setters
;; ===============

(defn on-connect! [connect-fn]
  (set! (.-onConnect AC) connect-fn))

(defn on-disconnect! [connect-fn]
  (set! (.-onDisconnect AC) connect-fn))

(defn on-message! [msg-fn]
  (set! (.-onMessage AC)
        (fn [device-id data]
          (msg-fn device-id (read-string data)))))

;; Commands
;; ========

(defn set-active-players [players]
  (.setActivePlayers AC players))

(defn message [device-id data]
  (.message AC device-id (pr-str data)))

;; Utils
;; =====

(defn device->player [device-id]
  (.convertDeviceIdToPlayerNumber AC device-id))

(defn player->device [device-id]
  (.convertPlayerNumberToDeviceId AC device-id))
