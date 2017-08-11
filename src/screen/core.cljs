(ns screen.core
  (:require [air-console.core :as ac]
            [monet.canvas :as canvas]))

;; Game
;; ====

(defonce state (atom {:player {:x 0}}))

;; Networking
;; ==========

(defn on-connect [device-id]
  (ac/set-active-players 2))

(defn on-disconnect [device-id]
  (ac/set-active-players 0))

(defn on-message [device-id [type payload]]
  (case type
    :move/up (swap! state update-in [:player :x] + payload)
    :move/down (swap! state update-in [:player :x] - payload)))

(ac/on-message! on-message)
(defonce foo
  (do
    (ac/on-connect! on-connect)
    (ac/on-disconnect! on-disconnect)
    (ac/on-message! on-message)))


;; Canvas

(def canvas-dom (.getElementById js/document "canvas"))
(def monet-canvas (canvas/init canvas-dom "2d"))

(canvas/add-entity
 monet-canvas :background
 (canvas/entity {:x 0 :y 0 :w 1200 :h 1200} ; val
                nil                       ; update function
                (fn [ctx val]             ; draw function
                  (-> ctx
                      (canvas/fill-style "#191d21")
                      (canvas/fill-rect val)))))

(canvas/add-entity
 monet-canvas
 :player
 (canvas/entity {:x 50 :y 50 :w 20 :h 20}
                nil
                (fn [ctx val]
                  (-> ctx
                      (canvas/fill-style "red")
                      (canvas/fill-rect (assoc val :x (get-in @state [:player :x])))))))
