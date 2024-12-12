(ns aoc2024.day06-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day06 :as d]
            [clojure.test :refer [deftest is]]))

(def example-input
  "....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...")

(deftest part1-test-example
  (is (= 41 (d/part1 example-input))))

(deftest part2-test-example
  (is (= 6 (d/part2 example-input))))

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2024 6)]
    (is (= answer1 (d/part1 d/input)))
    (is (= answer2 (d/part2 d/input)))))
