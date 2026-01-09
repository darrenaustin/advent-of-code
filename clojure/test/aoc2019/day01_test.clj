;; Test for aoc2019.day1
(ns aoc2019.day01-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2019.day01 :as d]
   [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    2 "12"
    2 "14"
    654 "1969"
    33583 "100756"))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    2 "14"
    966 "1969"
    50346 "100756"))

(def answers (delay (day-answers 2019 1)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
