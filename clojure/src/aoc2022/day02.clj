;; https://adventofcode.com/2022/day/2
(ns aoc2022.day02
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.math :refer [sum]]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2022 2))

(def ^:private rock     1)
(def ^:private paper    2)
(def ^:private scissors 3)

(def ^:private shape
  {\A rock, \B paper, \C scissors
   \X rock, \Y paper, \Z scissors})

(def ^:private beats
  {rock     paper
   paper    scissors
   scissors rock})

(def ^:private loses-to (c/vals->keys beats))

(defn- parse-round [s]
  (let [[c1 _ c2] s] [(shape c1) (shape c2)]))

(defn- player [[_ player]] player)

(defn- for-outcome [[opponent player]]
  (case player
    1 (loses-to opponent) ;; lose
    2 opponent            ;; draw
    3 (beats opponent)))  ;; win

(defn- score [[opponent _ :as round] player-fn]
  (let [player (player-fn round)]
    (+ player
       (condp = player
         opponent         3 ;; draw
         (beats opponent) 6 ;; win
         0))))              ;; lose

(defn- total-score [input player-fn]
  (->> (s/lines input)
       (map parse-round)
       (map #(score % player-fn))
       sum))

(defn part1 [input] (total-score input player))

(defn part2 [input] (total-score input for-outcome))
