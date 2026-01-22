;; Test for aoc2022.day4
(ns aoc2022.day04-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2022.day04 :as d]
   [clojure.test :refer :all]))

(def example
  "2-4,6-8
2-3,4-5
5-7,7-9
2-8,3-7
6-6,4-6
2-6,4-8")

(deftest part1-example
  (is (= 2 (d/part1 example))))

(deftest part2-example
  (is (= 4 (d/part2 example))))

(def answers (delay (day-answers 2022 4)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
