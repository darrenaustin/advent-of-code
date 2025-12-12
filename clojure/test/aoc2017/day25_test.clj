;; Test for aoc2017.day25
(ns aoc2017.day25-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2017.day25 :as d]
   [clojure.test :refer :all]))

(def example
  "Begin in state A.
Perform a diagnostic checksum after 6 steps.

In state A:
  If the current value is 0:
    - Write the value 1.
    - Move one slot to the right.
    - Continue with state B.
  If the current value is 1:
    - Write the value 0.
    - Move one slot to the left.
    - Continue with state B.

In state B:
  If the current value is 0:
    - Write the value 1.
    - Move one slot to the left.
    - Continue with state A.
  If the current value is 1:
    - Write the value 1.
    - Move one slot to the right.
    - Continue with state A.")

(deftest part1-example
  (is (= 3 (d/part1 example))))

(def answers (delay (day-answers 2017 25)))
(def input (delay (d/input)))

(deftest ^:slow part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
