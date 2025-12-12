;; Test for aoc2025.day4
(ns aoc2025.day04-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2025.day04 :as d]
   [clojure.test :refer :all]))

(def example
  "..@@.@@@@.
@@@.@.@.@@
@@@@@.@.@@
@.@@@@..@.
@@.@@@@.@@
.@@@@@@@.@
.@.@.@.@@@
@.@@@.@@@@
.@@@@@@@@.
@.@.@@@.@.")

(deftest part1-example
  (is (= 13 (d/part1 example))))

(deftest part2-example
  (is (= 43 (d/part2 example))))

(def answers (delay (day-answers 2025 4)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
