;; Test for aoc2018.day8
(ns aoc2018.day08-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2018.day08 :as d]
            [clojure.test :refer :all]))

(def example
  "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2")

(deftest part1-example
  (is (= 138 (d/part1 example))))

(deftest part2-example
  (is (= 66 (d/part2 example))))

(def answers (delay (day-answers 2018 8)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
