;; https://adventofcode.com/2024/day/3
(ns aoc2024.day03
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]))

(def input (d/day-input 2024 3))

(defn mult-instruction [instruction]
  (apply * (s/parse-nums instruction)))

(defn part1 [input]
  (m/sum (map mult-instruction (re-seq #"mul\(\d+,\d+\)" input))))

(defn part2 [input]
  (loop [commands (re-seq #"mul\(\d+,\d+\)|do\(\)|don\'t\(\)" input)
         sum 0]
    (if-let [command (first commands)]
      (case command
        "do()" (recur (rest commands) sum)
        "don't()" (recur (drop-while #(not= % "do()") commands) sum)
        (recur (rest commands) (+ sum (mult-instruction command))))
      sum)))
