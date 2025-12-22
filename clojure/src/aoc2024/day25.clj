;; https://adventofcode.com/2024/day/25
(ns aoc2024.day25
  (:require
   [aoc.day :as d]
   [aoc.util.collection :refer [count-where]]
   [aoc.util.matrix :refer [transpose]]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2024 25))

(defn grid->pins [grid]
  (map #(- 6 (count-where #{\.} %))
       (transpose (s/lines grid))))

(defn parse [input]
  (let [groups (group-by first (str/split input #"\n\n"))]
    (map #(map grid->pins %) [(groups \#) (groups \.)])))

(defn fit? [[lock key]]
  (every? #(<= % 5) (map + lock key)))

(defn part1 [input]
  (let [[locks keys] (parse input)]
    (count-where fit? (for [l locks k keys] [l k]))))

(defn part2 [_] "🎄 Got em all! 🎉")
