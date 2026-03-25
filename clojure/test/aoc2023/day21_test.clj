;; Test for aoc2023.day21
(ns aoc2023.day21-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2023.day21 :as d]
   [clojure.test :refer :all]))

(def example
  "...........
.....###.#.
.###.##..#.
..#.#...#..
....#.#....
.##..S####.
.##..#...#.
.......##..
.##.#.####.
.##..##.##.
...........")

(deftest plots-within-example
  (are [expected steps]
       (= expected (d/plots-within example steps))
    16 6
    50 10
    1594 50
    6536 100
    167004 500

    ;; These are too expensive to brute force, and
    ;; the quadratic trick only works for the main
    ;; input.

    ;; 668697 1000
    ;; 16733044 5000
    ))

(def answers (delay (day-answers 2023 21)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
