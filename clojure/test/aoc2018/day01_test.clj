;; Test for aoc2018.day1
(ns aoc2018.day01-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2018.day01 :as d]
   [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    3 "+1, -2, +3, +1"
    3 "+1, +1, +1"
    0 "+1, +1, -2"
    -6 "-1, -2, -3"))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    2 "+1, -2, +3, +1"
    0 "+1, -1"
    10 "+3, +3, +4, -2, -4"
    5 "-6, +3, +8, +5, -6"
    14 "+7, +7, -2, -7, -4"))

(def answers (delay (day-answers 2018 1)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
