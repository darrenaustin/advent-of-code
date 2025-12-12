;; Test for aoc2025.day3
(ns aoc2025.day03-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2025.day03 :as d]
   [clojure.test :refer :all]))

(def example
  "987654321111111
811111111111119
234234234234278
818181911112111")

(deftest part1-example
  (is (= 357 (d/part1 example))))

(deftest part2-example
  (is (= 3121910778619 (d/part2 example))))

(def answers (delay (day-answers 2025 3)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
