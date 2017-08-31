(ns hangman.views.hangman
  (:require [re-frame.core :refer [subscribe]]))

(def WIDTH 200)
(def HEIGHT (* WIDTH (/ 46 39)))

(def pillar
  [:g#Base
   [:g
    {:transform "translate(50.000000, 2.000000)"}
    [:g#Pole
     {:transform
      "translate(14.000000, 44.500000) rotate(-90.000000) translate(-14.000000, -44.500000) translate(-30.500000, 41.500000)"}
     [:path#Line
      {:stroke-linecap "square"
       :stroke-width "6"
       :stroke "#000000"
       :d "M6,3 L88,3"}]
     [:circle#Oval {:r "3" :cy "3" :cx "3" :fill "#000000"}]]
    [:g#Foot-left
     {:transform
      "translate(8.212961, 77.049856) rotate(-65.000000) translate(-8.212961, -77.049856) translate(-4.787039, 74.049856)"}
     [:path#Line
      {:stroke-linecap "square"
       :stroke-width "6"
       :stroke "#000000"
       :d "M6.39084374,3 L25.3990416,3"}]
     [:ellipse#Oval
      {:ry "3"
       :rx "3.38048256"
       :cy "3"
       :cx "3.38048256"
       :fill "#000000"}]]
    [:g#Foot-right
     {:transform
      "translate(20.212961, 77.049856) rotate(-115.000000) translate(-20.212961, -77.049856) translate(7.212961, 74.049856)"}
     [:path#Line
      {:stroke-linecap "square"
       :stroke-width "6"
       :stroke "#000000"
       :d "M6.39084374,3 L25.3990416,3"}]
     [:ellipse#Oval
      {:ry "3"
       :rx "3.38048256"
       :cy "3"
       :cx "3.38048256"
       :fill "#000000"}]]]
   [:g#Top
    {:transform "translate(4.000000, 0.000000)"}
    [:path#Line
     {:stroke-linecap "square"
      :stroke-width "6"
      :stroke "#000000"
      :d "M5.90163934,3 L60,3"}]
    [:ellipse#Oval
     {:ry "3"
      :rx "2.95081967"
      :cy "3"
      :cx "2.95081967"
      :fill "#000000"}]
    [:path#Support
     {:stroke-linecap "square"
      :stroke-width "6"
      :stroke "#000000"
      :d "M30,5 L58,33"}]]])

(def noose
  [:path#Noose
   {:stroke-linecap "square"
    :stroke-width "3"
    :stroke "#000000"
    :d "M14.5,3.51724138 L14.5,32.4827586"}])

(def body
  [:path#Body
   {:fill "#000000"
    :d
    "M13.9399527,32.5417392 L13.9399527,32.5417392 L13.9399527,32.5417392 C17.4100032,32.5417392 20.223035,35.3547709 20.223035,38.8248214 C20.223035,38.8815487 20.2222667,38.9382734 20.2207304,38.99498 L19.5356344,64.2827779 L19.5356344,64.2827779 C19.4494203,67.4650533 16.8448669,70 13.6614238,70 L13.6614238,70 L13.6614238,70 C10.4914883,70 7.91095144,67.450806 7.87221604,64.2811072 L7.56322918,38.9968658 L7.56322918,38.9968658 C7.52019132,35.4750986 10.3402586,32.5852532 13.8620258,32.5422153 C13.8880002,32.5418979 13.9139764,32.5417392 13.9399527,32.5417392 Z"}])

(def left-arm
  [:path#Left-arm
   {:fill "#000000"
    :d
    "M8.01370305,36.6383336 C1.82241418,44.5049521 -0.697774681,52.2615181 0.453136474,59.9080316 C2.69723076,62.567516 4.62935361,62.567516 6.249505,59.9080316 C6.20857718,54.3436564 6.73596926,50.6975897 7.83168123,48.9698313 L8.01370305,36.6383336 Z"}])

(def right-arm
  [:path#Right-arm
   {:fill "#000000"
    :d
    "M20.9270737,48.7269075 C22.0914565,54.8765189 22.582453,58.3626629 22.4000633,59.1853396 C24.5865429,61.7536291 26.5811562,61.6815248 28.3839032,58.9690264 C27.4667354,44.5709481 23.9836129,36.112211 17.9345357,33.5928151 C16.8558541,33.1291496 15.9575021,32.9691465 15.2394798,33.1128059 L20.9270737,48.7269075 Z"}])

(def left-leg
  [:path#Left-leg
   {:fill "#000000"
    :d
    "M8.58695769,66.882818 C7.3900833,75.3211777 6.9069451,80.266564 7.13754308,81.7189769 C8.84944688,84.27041 10.7527661,84.27041 12.8475007,81.7189769 C13.4945924,73.880281 13.8828747,69.7655654 14.0123476,69.3748299 L8.58695769,66.882818 Z"}])

(def right-leg
  [:path#Right-leg
   {:fill "#000000"
    :d
    "M13.9730969,69.8515626 L14.5287399,82.3484195 C16.5393853,84.7729691 18.541172,84.7729691 20.5341,82.3484195 L19.3216208,65.3818869 L13.9730969,69.8515626 Z"}])

(def head [:circle#Head {:r "6.5" :cy "31.5" :cx "7.5" :fill "#000000"}])

(def HANGMAN [pillar noose body left-arm right-arm left-leg right-leg head])

(defn component []
  (let [failures (subscribe [:failed-guesses])]
    (fn []
      [:div.u-centered.u-margin-top
       (into
        [:svg.hangman
         {:view-box "0 0 78 92" :height (str HEIGHT "px") :width (str WIDTH "px")}]
        (take (count @failures) HANGMAN))])))
