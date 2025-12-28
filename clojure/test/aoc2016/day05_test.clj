;; Test for aoc2016.day5
(ns aoc2016.day05-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day05 :as d]
   [clojure.test :refer :all]))

(def example
  "abc")

(deftest ^:slow part1-example
  (is (= "18f47a30" (d/part1 example))))

(deftest ^:slow part2-example
  (is (= "05ace8e3" (d/part2 example))))

(def answers (delay (day-answers 2016 5)))
(def input (delay (d/input)))

(deftest ^:slow part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
