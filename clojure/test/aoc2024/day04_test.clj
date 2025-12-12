(ns aoc2024.day04-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2024.day04 :as d]
   [clojure.test :refer :all]))

(def example-input
  "MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX")

(deftest part1-test-example
  (is (= 18 (d/part1 example-input))))

(deftest part2-test-example
  (is (= 9 (d/part2 example-input))))

(def answers (delay (day-answers 2024 4)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
