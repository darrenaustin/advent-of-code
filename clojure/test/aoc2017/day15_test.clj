;; Test for aoc2017.day15
(ns aoc2017.day15-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2017.day15 :as d]
            [clojure.test :refer :all]))

(def example
  "Generator A starts with 65
Generator B starts with 8921
")

(deftest part1-example
  (is (= 588 (d/part1 example))))

(deftest part2-example
  (is (= 309 (d/part2 example))))

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2017 15) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
