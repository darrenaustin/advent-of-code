;; Test for aoc2017.day24
(ns aoc2017.day24-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2017.day24 :as d]
            [clojure.test :refer :all]))

(def example
  "0/2
2/2
2/3
3/4
3/5
0/1
10/1
9/10")

(deftest part1-example
  (is (= 31 (d/part1 example))))

(deftest part2-example
  (is (= 19 (d/part2 example))))

(def answers (delay (day-answers 2017 24)))
(def input (delay (d/input)))

(deftest ^:slow part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
