;; Test for aoc2015.day5
(ns aoc2015.day05-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2015.day05 :as d]
   [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    1 "ugknbfddgicrmopn"
    1 "aaa"
    0 "jchzalrnumimnmhp"
    0 "haegwjzuvuyypxyu"
    0 "dvszwmarrgswjxmb"))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    1 "qjhvhtzxzqqjkmpb"
    1 "xxyxx"
    0 "uurcxstgmygtbstg"
    0 "ieodomkazucvgmuy"))

(def answers (delay (day-answers 2015 5)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
