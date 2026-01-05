;; Test for aoc2016.day14
(ns aoc2016.day14-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day14 :as d]
   [clojure.test :refer :all]))

(def example
  "abc")

(deftest ^:slow part1-example
  (is (= 22728 (d/part1 example))))

(deftest ^:slow part2-example
  (is (= 22551 (d/part2 example))))

(def answers (delay (day-answers 2016 14)))
(def input (delay (d/input)))

(deftest ^:slow part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
