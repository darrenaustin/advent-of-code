;; Test for aoc2016.day20
(ns aoc2016.day20-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day20 :as d]
   [clojure.test :refer :all]))

(def example
  "5-8
0-2
4-7")

(deftest part1-example
  (is (= 3 (d/part1 example))))

(def answers (delay (day-answers 2016 20)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
