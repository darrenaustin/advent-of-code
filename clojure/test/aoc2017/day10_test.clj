;; Test for aoc2017.day10
(ns aoc2017.day10-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2017.day10 :as d]
   [clojure.test :refer :all]))

(def example
  "3, 4, 1, 5")

(deftest part1-example
  (is (= 12 (d/part1 example 5))))

(def answers (delay (day-answers 2017 10)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
