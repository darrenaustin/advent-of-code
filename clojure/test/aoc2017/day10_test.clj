;; Test for aoc2017.day10
(ns aoc2017.day10-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2017.day10 :as d]
            [clojure.test :refer :all]))

(def example
  "3, 4, 1, 5")

(deftest part1-example
  (is (= 12 (d/part1 example 5))))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2017 10) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
