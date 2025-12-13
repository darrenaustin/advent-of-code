;; https://adventofcode.com/2015/day/5
  (ns aoc2015.day05
    (:require
     [aoc.day :as d]
     [aoc.util.string :as s]))

(defn input [] (d/day-input 2015 5))

(defn ridiculous-nice-string? [s]
  (and (>= (count (filter (set "aeiou") s)) 3)
       (re-find #"(.)\1" s)
       (not (re-find #"ab|cd|pq|xy" s))))

(defn nice-string? [s]
  (and (re-find #"(..).*\1" s)
       (re-find #"(.).\1" s)))

(defn part1 [input]
  (count (filter ridiculous-nice-string? (s/lines input))))

(defn part2 [input]
  (count (filter nice-string? (s/lines input))))
