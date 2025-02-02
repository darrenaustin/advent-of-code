;; https://adventofcode.com/2019/day/2
(ns aoc2019.day02
  (:require [aoc.day :as d]
            [aoc2019.intcode :as i]))

(defn input [] (d/day-input 2019 2))

(defn result [program & inputs]
  (first (:mem (i/execute (apply assoc program inputs)))))

(defn part1 [input]
  (result (i/parse input) 1 12, 2 2))

(defn part2 [input]
  (let [program (i/parse input)
        [noun verb] (first
                      (for [noun (range 100) verb (range 100)
                            :when (= 19690720 (result program 1 noun, 2 verb))]
                        [noun verb]))]
    (+ (* 100 noun) verb)))
