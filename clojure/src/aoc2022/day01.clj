;; https://adventofcode.com/2022/day/1
(ns aoc2022.day01
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2022 1))

(defn elf-calories [input]
  (map (comp m/sum s/ints) (s/blocks input)))

(defn part1 [input]
  (->> input
       elf-calories
       (apply max)))

(defn part2 [input]
  (->> input
       elf-calories
       (sort >)
       (take 3)
       m/sum))
