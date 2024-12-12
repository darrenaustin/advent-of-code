(ns aoc2015.day03-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2015.day03 :as d]
            [clojure.test :refer [deftest are is]]))

(deftest part1-example
  (are [expected input] (= expected (d/part1 input))
    2 ">"
    4 "^>v<"
    2 "^v^v^v^v^v"))

(deftest part2-example
  (are [expected input] (= expected (d/part2 input))
    3  "^v"
    3  "^>v<"
    11 "^v^v^v^v^v"))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2015 3)]
    (is (= answer1 (d/part1 d/input)))
    (is (= answer2 (d/part2 d/input)))))
