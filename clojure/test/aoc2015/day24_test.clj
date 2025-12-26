;; Test for aoc2015.day24
(ns aoc2015.day24-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2015.day24 :as d]
   [clojure.test :refer :all]))

(def example
  "1
2
3
4
5
7
8
9
10
11")

(deftest part1-example
  (is (= 99 (d/part1 example))))

(deftest part2-example
  (is (= 44 (d/part2 example))))

(def answers (delay (day-answers 2015 24)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
