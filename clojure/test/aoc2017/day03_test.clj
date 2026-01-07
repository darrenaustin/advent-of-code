;; Test for aoc2017.day3
(ns aoc2017.day03-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2017.day03 :as d]
   [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    0 "1"
    3 "12"
    2 "23"
    31 "1024"))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    10 "5"
    25 "24"
    122 "60"
    806 "800"))

(def answers (delay (day-answers 2017 3)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
