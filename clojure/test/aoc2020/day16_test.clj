;; Test for aoc2020.day16
(ns aoc2020.day16-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2020.day16 :as d]
   [clojure.test :refer :all]))

(def example
  "class: 1-3 or 5-7
row: 6-11 or 33-44
seat: 13-40 or 45-50

your ticket:
7,1,14

nearby tickets:
7,3,47
40,4,50
55,2,20
38,6,12")

(deftest part1-example
  (is (= 71 (d/part1 example))))

(def answers (delay (day-answers 2020 16)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
