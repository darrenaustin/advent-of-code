;; https://adventofcode.com/2024/day/25
(ns aoc2024.day25
  (:require [aoc.day :as d]
            [aoc.util.collection :as c]
            [clojure.string :as str]))

(defn input [] (d/day-input 2024 25))

(defn grid->pins [grid]
  (map #(- 6 (count (filter #{\.} %)))
       (c/transpose (str/split-lines grid))))

(defn parse [input]
  (let [grids  (str/split input #"\n\n")
        groups (group-by first grids)]
    (map #(map grid->pins %) [(groups \#) (groups \.)])))

(defn fit? [lock key]
  (every? identity (map (fn [l k] (<= (+ l k) 5)) lock key)))

(defn part1 [input]
  (let [[locks keys] (parse input)]
    (count (for [l locks k keys :when (fit? l k)] 1))))

(defn part2 [_] "🎄 Got em all! 🎉")
