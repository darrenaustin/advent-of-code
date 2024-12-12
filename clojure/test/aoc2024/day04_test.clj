(ns aoc2024.day04-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day04 :as d]
            [clojure.test :refer [deftest is]]))

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

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2024 4)]
    (is (= answer1 (d/part1 d/input)))
    (is (= answer2 (d/part2 d/input)))))
