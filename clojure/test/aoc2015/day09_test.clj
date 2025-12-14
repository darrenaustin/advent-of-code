;; Test for aoc2015.day9
(ns aoc2015.day09-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2015.day09 :as d]
   [clojure.test :refer :all]))

(def example
  "London to Dublin = 464
London to Belfast = 518
Dublin to Belfast = 141")

(deftest part1-example
  (is (= 605 (d/part1 example))))

(deftest part2-example
  (is (= 982 (d/part2 example))))

(def answers (delay (day-answers 2015 9)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
