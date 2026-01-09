;; Test for aoc2018.day3
(ns aoc2018.day03-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2018.day03 :as d]
   [clojure.test :refer :all]))

(def example
  "#1 @ 1,3: 4x4
#2 @ 3,1: 4x4
#3 @ 5,5: 2x2")

(deftest part1-example
  (is (= 4 (d/part1 example))))

(deftest part2-example
  (is (= 3 (d/part2 example))))

(def answers (delay (day-answers 2018 3)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
