;; Test for aoc2018.day16
(ns aoc2018.day16-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2018.day16 :as d]
            [clojure.test :refer :all]))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2018 16) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
