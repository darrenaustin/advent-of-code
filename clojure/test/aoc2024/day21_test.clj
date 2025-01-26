;; Test for aoc2024.day21
(ns aoc2024.day21-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day21 :as d]
            [clojure.test :refer :all]))

(def example
  "029A
980A
179A
456A
379A")

(deftest part1-example
  (is (= 126384 (d/part1 example))))

(def answers (delay (day-answers 2024 21)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
