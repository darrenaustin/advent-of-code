;; Test for aoc2017.day14
(ns aoc2017.day14-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2017.day14 :as d]
            [clojure.test :refer :all]))

(def example "flqrgnkx")

(deftest ^:slow part1-example
  (is (= 8108 (d/part1 example))))

(deftest ^:slow part2-example
  (is (= 1242 (d/part2 example))))

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2017 14) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
