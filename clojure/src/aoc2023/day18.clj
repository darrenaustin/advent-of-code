;; https://adventofcode.com/2023/day/18
(ns aoc2023.day18
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.pos :as p]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2023 18))

(defn- dir-for [s]
  (case s ("R" "0") [1 0], ("D" "1") [0 1], ("L" "2") [-1 0], ("U" "3") [0 -1]))

(defn- parse-plan [line]
  (let [[dir dist] (str/split line #" ")]
    [(s/int dist) (dir-for dir)]))

(defn- parse-plan-color [line]
  (let [[_ dist dir] (re-find #"\(\#(.{5})(.)\)" line)]
    [(Long/parseLong dist 16) (dir-for dir)]))

(defn- dirs->points [ds]
  (reductions (fn [p [dist dir]] (p/pos+ p (p/pos* dist dir))) [0 0] ds))

;; https://en.wikipedia.org/wiki/Shoelace_formula
(defn- shoelace-area [points]
  (let [[perimeter area]
        (reduce (fn [[p a] [[ax ay] [bx by]]]
                  [(+ p (m/manhattan-distance [ax ay] [bx by]))
                   (+ a (* ax by) (- (* bx ay)))])
                [0 0]
                (partition 2 1 points))]
    (inc (quot (+ perimeter (abs area)) 2))))

(defn- area [input parse-fn]
  (->> (s/lines input)
       (map parse-fn)
       dirs->points
       shoelace-area))

(defn part1 [input] (area input parse-plan))

(defn part2 [input] (area input parse-plan-color))
