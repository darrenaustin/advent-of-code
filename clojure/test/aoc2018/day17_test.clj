;; Test for aoc2018.day17
(ns aoc2018.day17-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2018.day17 :as d]
            [clojure.test :refer :all]))

(def example
  "x=495, y=2..7
y=7, x=495..501
x=501, y=3..7
x=498, y=2..4
x=506, y=1..2
x=498, y=10..13
x=504, y=10..13
y=13, x=498..504")

(deftest part1-example
  (is (= 57 (d/part1 example))))

(deftest part2-example
  (is (= 29 (d/part2 example))))

(def answers (delay (day-answers 2018 17)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
