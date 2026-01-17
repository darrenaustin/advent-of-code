;; Test for aoc2020.day6
(ns aoc2020.day06-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2020.day06 :as d]
   [clojure.test :refer :all]))

(def example
  "abc

a
b
c

ab
ac

a
a
a
a

b")

(deftest part1-example
  (is (= 11 (d/part1 example))))

(deftest part2-example
  (is (= 6 (d/part2 example))))

(def answers (delay (day-answers 2020 6)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
