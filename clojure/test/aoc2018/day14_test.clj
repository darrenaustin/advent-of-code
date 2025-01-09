;; Test for aoc2018.day14
(ns aoc2018.day14-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2018.day14 :as d]
            [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
    (= expected (d/part1 input))
    "5158916779" "9"
    "0124515891" "5"
    "9251071085" "18"
    "5941429882" "2018"))

(deftest part2-examples
  (are [expected input]
    (= expected (d/part2 input))
    9 "51589"
    5 "01245"
    18 "92510"
    2018 "59414"))

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2018 14) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
