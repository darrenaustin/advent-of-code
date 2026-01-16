;; Test for aoc2020.day1
(ns aoc2020.day01-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2020.day01 :as d]
   [clojure.test :refer :all]))

(def example
  "1721
979
366
299
675
1456")

(deftest part1-example
  (is (= 514579 (d/part1 example))))

(deftest part2-example
  (is (= 241861950 (d/part2 example))))

(def answers (delay (day-answers 2020 1)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
