;; Test for aoc2018.day18
(ns aoc2018.day18-test
  (:require
   [aoc.day :refer [day-answers]]
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

(def answers (delay (day-answers 2018 18)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
