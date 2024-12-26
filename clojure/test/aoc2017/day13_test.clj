;; Test for aoc2017.day13
(ns aoc2017.day13-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2017.day13 :as d]
            [clojure.test :refer :all]))

(def example
  "0: 3
1: 2
4: 4
6: 4")

(deftest part1-example
  (is (= 24 (d/part1 example))))

(deftest part2-example
  (is (= 10 (d/part2 example))))

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2017 13) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
