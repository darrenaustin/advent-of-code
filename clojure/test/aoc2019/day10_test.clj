;; Test for aoc2019.day10
(ns aoc2019.day10-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2019.day10 :as d]
   [clojure.test :refer :all]))

(def example
  ".#..##.###...#######
##.############..##.
.#.######.########.#
.###.#######.####.#.
#####.##.#.##.###.##
..#####..#.#########
####################
#.####....###.#.#.##
##.#################
#####.##.###..####..
..######..##.#######
####.##.####...##..#
.#####..#.######.###
##...#.##########...
#.##########.#######
.####.#.###.###.#.##
....##.##.###..#####
.#.#.###########.###
#.#.#.#####.####.###
###.##.####.##.#..##")

(deftest part1-example
  (is (= 210 (d/part1 example))))

(deftest part2-example
  (is (= 802 (d/part2 example))))

(def answers (delay (day-answers 2019 10)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
