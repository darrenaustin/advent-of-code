;; Test for aoc2017.day17
(ns aoc2017.day17-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2017.day17 :as d]
            [clojure.test :refer :all]))

(def example "3")

(deftest part1-example
  (is (= 638 (d/part1 example))))

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2017 17) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
