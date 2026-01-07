;; Test for aoc2017.day5
(ns aoc2017.day05-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2017.day05 :as d]
   [clojure.test :refer :all]))

(def example
  "0
3
0
1
-3")

(deftest part1-example
  (is (= 5 (d/part1 example))))

(deftest part2-example
  (is (= 10 (d/part2 example))))

(def answers (delay (day-answers 2017 5)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
