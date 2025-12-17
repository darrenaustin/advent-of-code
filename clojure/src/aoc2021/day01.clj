;; https://adventofcode.com/2021/day/1
(ns aoc2021.day01
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2021 1))

;; This elegant solution is cribbed from:
;; https://github.com/tschady/advent-of-code/blob/main/src/aoc/2021/d01.clj
(defn part1 [input]
  (count (filter pos? (m/intervals (s/ints input)))))

(defn part2 [input]
  (count (filter pos? (m/intervals 3 (s/ints input)))))

;; My less elegant original solution:
;;
;; (defn part1 [input]
;;   (->> (s/ints input)
;;        (partition 2 1)
;;        (filter #(apply < %))
;;        count))

;; (defn part2 [input]
;;   (->> (s/ints input)
;;        (partition 3 1)
;;        (map m/sum)
;;        (partition 2 1)
;;        (filter #(apply < %))
;;        count))
