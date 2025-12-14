;; Test for aoc2015.day10
(ns aoc2015.day10-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2015.day10 :as d]
   [clojure.test :refer :all]))

(deftest look-and-say-test
  (are [expected input]
       (= expected (d/look-and-say input))
    "11" "1"
    "21" "11"
    "1211" "21"
    "111221" "1211"
    "312211" "111221"))

(def answers (delay (day-answers 2015 10)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
