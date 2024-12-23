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

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2018 8) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
