;; Test for aoc2017.day17
(ns aoc2017.day17-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2017.day17 :as d]
            [clojure.test :refer :all]))

(def example "3")

(deftest part1-example
  (is (= 638 (d/part1 example))))

(def answers (delay (day-answers 2017 17)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
