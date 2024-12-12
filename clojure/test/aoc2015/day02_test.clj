(ns aoc2015.day02-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2015.day02 :as d]
            [clojure.test :refer [deftest are is]]))

(deftest part1-examples
  (are [expected input] (= expected (d/part1 input))
    58 "2x3x4"
    43 "1x1x10"))

(deftest part2-examples
  (are [expected input] (= expected (d/part2 input))
    34 "2x3x4"
    14 "1x1x10"))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2015 2)]
    (is (= answer1 (d/part1 d/input)))
    (is (= answer2 (d/part2 d/input)))))
