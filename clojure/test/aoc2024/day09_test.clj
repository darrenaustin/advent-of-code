(ns aoc2024.day09-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2024.day09 :as d]
   [clojure.test :refer :all]))

(def example-input
  "2333133121414131402")

(deftest part1-example
  (is (= 1928 (d/part1 example-input))))

(deftest part2-example
  (is (= 2858 (d/part2 example-input))))

(def answers (delay (day-answers 2024 9)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
