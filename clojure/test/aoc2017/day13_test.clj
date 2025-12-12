;; Test for aoc2017.day13
(ns aoc2017.day13-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2017.day13 :as d]
   [clojure.test :refer :all]))

(def example
  "0: 3
1: 2
4: 4
6: 4")

(deftest part1-example
  (is (= 24 (d/part1 example))))

(deftest part2-example
  (is (= 10 (d/part2 example))))

(def answers (delay (day-answers 2017 13)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
