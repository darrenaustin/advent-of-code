;; Test for aoc2020.day18
(ns aoc2020.day18-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2020.day18 :as d]
   [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    71    "1 + 2 * 3 + 4 * 5 + 6"
    51    "1 + (2 * 3) + (4 * (5 + 6))"
    26    "2 * 3 + (4 * 5)"
    437   "5 + (8 * 3 + 9 + 3 * 4 * 3)"
    12240 "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"
    13632 "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    231    "1 + 2 * 3 + 4 * 5 + 6"
    51     "1 + (2 * 3) + (4 * (5 + 6))"
    46     "2 * 3 + (4 * 5)"
    1445   "5 + (8 * 3 + 9 + 3 * 4 * 3)"
    669060 "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"
    23340  "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))

(def answers (delay (day-answers 2020 18)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
