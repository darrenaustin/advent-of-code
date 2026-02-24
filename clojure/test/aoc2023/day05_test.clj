;; Test for aoc2023.day5
(ns aoc2023.day05-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2023.day05 :as d]
   [clojure.test :refer :all]))

(def example
  "seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4")

(deftest part1-example
  (is (= 35 (d/part1 example))))

(deftest part2-example
  (is (= 46 (d/part2 example))))

(def answers (delay (day-answers 2023 5)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
