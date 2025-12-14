;; Test for aoc2015.day8
(ns aoc2015.day08-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2015.day08 :as d]
   [clojure.test :refer :all]))

(deftest decoded-size-test
  (are [expected input]
       (= expected (d/decoded-size input))
    0 "\"\""
    3 "\"abc\""
    7 "\"aaa\\\"aaa\""
    1 "\"\\x27\""))

(deftest encoded-size-test
  (are [expected input]
       (= expected (d/encoded-size input))
    6  "\"\""
    9 "\"abc\""
    16 "\"aaa\\\"aaa\""
    11 "\"\\x27\""))

(def answers (delay (day-answers 2015 8)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
