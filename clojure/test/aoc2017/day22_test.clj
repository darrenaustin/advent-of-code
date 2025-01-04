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

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2017 22) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
