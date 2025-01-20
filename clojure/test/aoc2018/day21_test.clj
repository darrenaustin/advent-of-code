;; Test for aoc2018.day21
(ns aoc2018.day21-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2018.day21 :as d]
            [clojure.test :refer :all]))

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2018 21) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
