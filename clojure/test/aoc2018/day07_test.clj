;; Test for aoc2018.day7
(ns aoc2018.day07-test
  (:require [aoc.day :refer [day-answers]]
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

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2018 7) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))