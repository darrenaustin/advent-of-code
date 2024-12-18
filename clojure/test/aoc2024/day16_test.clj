;; Test for aoc2024.day16
(ns aoc2024.day16-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day16 :as d]
            [clojure.test :refer :all]))

(def example1
  "###############
#.......#....E#
#.#.###.#.###.#
#.....#.#...#.#
#.###.#####.#.#
#.#.#.......#.#
#.#.#####.###.#
#...........#.#
###.#.#####.#.#
#...#.....#.#.#
#.#.#.###.#.#.#
#.....#...#.#.#
#.###.#.#.#.#.#
#S..#.....#...#
###############")

(def example2
  "#################
#...#...#...#..E#
#.#.#.#.#.#.#.#.#
#.#.#.#...#...#.#
#.#.#.#.###.#.#.#
#...#.#.#.....#.#
#.#.#.#.#.#####.#
#.#...#.#.#.....#
#.#.#####.#.###.#
#.#.#.......#...#
#.#.###.#####.###
#.#.#...#.....#.#
#.#.#.#####.###.#
#.#.#.........#.#
#.#.#.#########.#
#S#.............#
#################")

(deftest part1-examples
  (are [expected input]
    (= expected (d/part1 input))
    7036 example1
    11048 example2))

(deftest part2-examples
  (are [expected input]
    (= expected (d/part2 input))
    45 example1
    64 example2))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2024 16)]
    (is (= answer1 (d/part1 d/input)))
    (is (= answer2 (d/part2 d/input)))))