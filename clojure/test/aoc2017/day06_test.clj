;; Test for aoc2017.day6
(ns aoc2017.day06-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2017.day06 :as d]
   [clojure.test :refer :all]))

(def example "0 2 7 0")

(deftest part1-example
  (is (= 5 (d/part1 example))))

(deftest part2-example
  (is (= 4 (d/part2 example))))

(def answers (delay (day-answers 2017 6)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
