;; Test for aoc2016.day1
(ns aoc2016.day01-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day01 :as d]
   [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    5 "R2, L3"
    2 "R2, R2, R2"
    12 "R5, L5, R5, R3"))

(deftest part2-example
  (is (= 4 (d/part2 "R8, R4, R4, R8"))))

(def answers (delay (day-answers 2016 1)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
