;; Test for aoc2017.day23
(ns aoc2017.day23-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2017.day23 :as d]
            [clojure.test :refer :all]))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2017 23) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
