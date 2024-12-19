(ns aoc2024.day08-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day08 :as d]
            [clojure.test :refer [deftest is]]))

(def example-input
  "............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............")

(deftest part1-example
  (is (= 14 (d/part1 example-input))))

(deftest part2-example
  (is (= 34 (d/part2 example-input))))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2024 8) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
