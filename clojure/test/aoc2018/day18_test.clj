;; Test for aoc2018.day18
(ns aoc2018.day18-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2018.day18 :as d]
            [clojure.test :refer :all]))

(def example
  ".#.#...|#.
.....#|##|
.|..|...#.
..|#.....#
#.#|||#|#|
...#.||...
.|....|...
||...#|.#|
|.||||..|.
...#.|..|.")

(deftest part1-example
  (is (= 1147 (d/part1 example))))

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2018 18) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
