;; Test for aoc2017.day22
(ns aoc2017.day22-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2017.day22 :as d]
            [clojure.test :refer :all]))

(def example
  "..#
#..
...")

(deftest part1-example
  (is (= 5587 (d/part1 example))))

(deftest ^:slow part2-example
  (is (= 2511944 (d/part2 example))))

(def answers (delay (day-answers 2017 22)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
