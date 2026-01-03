;; Test for aoc2016.day15
(ns aoc2016.day15-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day15 :as d]
   [clojure.test :refer :all]))

(def example
  "Disc #1 has 5 positions; at time=0, it is at position 4.
Disc #2 has 2 positions; at time=0, it is at position 1.")

(deftest part1-example
  (is (= 5 (d/part1 example))))

(def answers (delay (day-answers 2016 15)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
