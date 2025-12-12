;; Test for aoc2017.day15
(ns aoc2017.day15-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2017.day15 :as d]
   [clojure.test :refer :all]))

(def example
  "Generator A starts with 65
Generator B starts with 8921
")

(deftest ^:slow part1-example
  (is (= 588 (d/part1 example))))

(deftest ^:slow part2-example
  (is (= 309 (d/part2 example))))

(def answers (delay (day-answers 2017 15)))
(def input (delay (d/input)))

(deftest ^:slow part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
