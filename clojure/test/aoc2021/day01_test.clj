;; Test for aoc2021.day1
(ns aoc2021.day01-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2021.day01 :as d]
   [clojure.test :refer :all]))

(def example
  "199
200
208
210
200
207
240
269
260
263")

(deftest part1-example
  (is (= 7 (d/part1 example))))

(deftest part2-example
  (is (= 5 (d/part2 example))))

(def answers (delay (day-answers 2021 1)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
