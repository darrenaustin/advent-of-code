;; Test for aoc2018.day22
(ns aoc2018.day22-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2018.day22 :as d]
            [clojure.test :refer :all]))

(def example
  "depth: 510
target: 10,10")

(deftest part1-example
  (is (= 114 (d/part1 example))))

(deftest part2-example
  (is (= 45 (d/part2 example))))

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2018 22) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
