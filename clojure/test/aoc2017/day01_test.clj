;; Test for aoc2017.day1
(ns aoc2017.day01-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2017.day01 :as d]
   [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    3 "1122"
    4 "1111"
    0 "1234"
    9 "91212129"))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    6 "1212"
    0 "1221"
    4 "123425"
    12 "123123"
    4 "12131415"))

(def answers (delay (day-answers 2017 1)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
