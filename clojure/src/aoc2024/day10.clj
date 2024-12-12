;; https://adventofcode.com/2024/day/10
(ns aoc2024.day10
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.grid :refer :all]
   [aoc.util.math :as m]
   [aoc.util.string :as s]))

(def input (d/day-input 2024 10))

(defn update-summits-from [grid summits loc]
  (let [target (dec (grid loc))
        loc-summits (summits loc)]
    (reduce (fn [goals neighbor]
              (update goals neighbor concat loc-summits))
            summits
            (filter #(= target (grid %)) (orthogonal-from loc)))))

(defn summits-reachable-map [grid]
  (let [num-locs (c/group-by-value grid)]
    (reduce (fn [summits n]
              (reduce (partial update-summits-from grid)
                      summits
                      (num-locs n)))
            (into {} (map (fn [p] [p [p]]) (num-locs 9)))
            (range 9 0 -1))))

(defn count-trails [input coll-fn]
  (let [grid (parse-grid input s/digit)
        summits-reachable (summits-reachable-map grid)
        trailheads (locs-where grid #{0})]
    (m/sum (map (comp count coll-fn summits-reachable) trailheads))))

(defn part1 [input]
  (count-trails input set))

(defn part2 [input]
  (count-trails input identity))
