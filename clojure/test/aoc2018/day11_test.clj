;; Test for aoc2018.day11
(ns aoc2018.day11-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2018.day11 :as d]
            [clojure.test :refer :all]))

(deftest power-test
  (are [expected coords serial]
    (= expected (d/power serial coords))
    4 [3 5] 8
    -5 [122 79] 57
    0 [217 196] 39
    4 [101 153] 71))

(deftest part1-examples
  (are [expected input]
    (= expected (d/part1 input))
    "33,45" "18"
    "21,61" "42"))

(deftest ^:slow part2-examples
  (are [expected input]
    (= expected (d/part2 input))
    "90,269,16" "18"
    "232,251,12" "42"))

(def answers (delay (day-answers 2018 11)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
