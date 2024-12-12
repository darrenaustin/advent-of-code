;; Test for aoc2024.day12
(ns aoc2024.day12-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day12 :as d]
            [clojure.test :refer :all]))

(def example-input1
  "AAAA
BBCD
BBCC
EEEC")

(def example-input2
  "OOOOO
OXOXO
OOOOO
OXOXO
OOOOO")

(def example-input3
  "RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE")

(def example-input4
  "EEEEE
EXXXX
EEEEE
EXXXX
EEEEE")

(def example-input5
  "AAAAAA
AAABBA
AAABBA
ABBAAA
ABBAAA
AAAAAA")

(deftest part1-examples
  (are [expected input]
    (= expected (d/part1 input))
    140 example-input1
    772 example-input2
    1930 example-input3))

(deftest part2-examples
  (are [expected input]
    (= expected (d/part2 input))
    80 example-input1
    436 example-input2
    1206 example-input3
    236 example-input4
    368 example-input5))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2024 12)]
    (is (= answer1 (d/part1 d/input)))
    (is (= answer2 (d/part2 d/input)))))
