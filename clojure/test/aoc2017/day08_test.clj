;; Test for aoc2017.day8
(ns aoc2017.day08-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2017.day08 :as d]
   [clojure.test :refer :all]))

(def example
  "b inc 5 if a > 1
a inc 1 if b < 5
c dec -10 if a >= 1
c inc -20 if c == 10")

(deftest part1-example
  (is (= 1 (d/part1 example))))

(deftest part2-example
  (is (= 10 (d/part2 example))))

(def answers (delay (day-answers 2017 8)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
