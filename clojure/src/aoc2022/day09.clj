;; https://adventofcode.com/2022/day/9
(ns aoc2022.day09
  (:require
   [aoc.day :as d]
   [aoc.util.collection :refer [nth>>]]
   [aoc.util.pos :as p]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2022 9))

(defn- parse-steps [line]
  (repeat (s/int line) (p/dir->offset (first line))))

(defn- head-path [input]
  (reductions p/pos+ p/origin
              (mapcat parse-steps (s/lines input))))

(defn- step-knot [knot head]
  (let [dir (p/pos- head knot)]
    (if (p/unit-step? dir)
      knot
      (p/pos+ knot (p/sign dir)))))

(defn- num-tail-positions [input knots]
  (->> (head-path input)
       (iterate #(reductions step-knot p/origin %))
       (nth>> (dec knots))
       distinct
       count))

(defn part1 [input] (num-tail-positions input 2))

(defn part2 [input] (num-tail-positions input 10))
