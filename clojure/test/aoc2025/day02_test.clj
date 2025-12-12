;; Test for aoc2025.day2
(ns aoc2025.day02-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2025.day02 :as d]
   [clojure.test :refer :all]))

(def example
  "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124")

(deftest part1-example
  (is (= 1227775554 (d/part1 example))))

(deftest part2-example
  (is (= 4174379265 (d/part2 example))))

(def answers (delay (day-answers 2025 2)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
