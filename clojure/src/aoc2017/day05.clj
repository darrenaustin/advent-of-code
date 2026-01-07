;; https://adventofcode.com/2017/day/5
(ns aoc2017.day05
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2017 5))

(defn steps-to-exit [input offset-update]
  (loop [jumps (transient (s/ints input)), idx 0, steps 0]
    (if-not (< -1 idx (count jumps))
      steps
      (let [offset (get jumps idx)]
        (recur (assoc! jumps idx (offset-update offset))
               (+ idx offset)
               (inc steps))))))

(defn part1 [input] (steps-to-exit input inc))

(defn part2 [input] (steps-to-exit input #(if (>= % 3) (dec %) (inc %))))
