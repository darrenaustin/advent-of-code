;; Test for aoc2016.day8
(ns aoc2016.day08-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day08 :as d]
   [clojure.test :refer :all]))

(def example
  "rect 3x2
rotate column x=1 by 1
rotate row y=0 by 4
rotate column x=1 by 1")

(deftest part1-example
  (is (= 6 (d/part1 example [7 3]))))

(def answers (delay (day-answers 2016 8)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
