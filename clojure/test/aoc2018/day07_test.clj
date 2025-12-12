;; Test for aoc2018.day7
(ns aoc2018.day07-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2018.day07 :as d]
   [clojure.test :refer :all]))

(def example
  "Step C must be finished before step A can begin.
Step C must be finished before step F can begin.
Step A must be finished before step B can begin.
Step A must be finished before step D can begin.
Step B must be finished before step E can begin.
Step D must be finished before step E can begin.
Step F must be finished before step E can begin.")

(deftest part1-example
  (is (= "CABDFE" (d/part1 example))))

(deftest part2-example
  (is (= 15 (d/part2 example 2 0))))

(def answers (delay (day-answers 2018 7)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
