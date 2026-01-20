;; Test for aoc2020.day17
(ns aoc2020.day17-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2020.day17 :as d]
   [clojure.test :refer :all]))

(def example
  ".#.
..#
###")

(deftest part1-example
  (is (= 112 (d/part1 example))))

(deftest part2-example
  (is (= 848 (d/part2 example))))

(def answers (delay (day-answers 2020 17)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
