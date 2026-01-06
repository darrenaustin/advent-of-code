;; Test for aoc2017.day2
(ns aoc2017.day02-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2017.day02 :as d]
   [clojure.test :refer :all]))

(def example1
  "5 1 9 5
7 5 3
2 4 6 8")

(def example2
  "5 9 2 8
9 4 7 3
3 8 6 5")

(deftest part1-example
  (is (= 18 (d/part1 example1))))

(deftest part2-example
  (is (= 9 (d/part2 example2))))

(def answers (delay (day-answers 2017 2)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
