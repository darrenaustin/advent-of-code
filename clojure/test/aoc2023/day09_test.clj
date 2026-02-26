;; Test for aoc2023.day9
(ns aoc2023.day09-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2023.day09 :as d]
   [clojure.test :refer :all]))

(def example
  "0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45")

(deftest part1-example
  (is (= 114 (d/part1 example))))

(deftest part2-example
  (is (= 2 (d/part2 example))))

(def answers (delay (day-answers 2023 9)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
