;; Test for aoc2018.day12
(ns aoc2018.day12-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2018.day12 :as d]
            [clojure.test :refer :all]))

(def example
  "initial state: #..#.#..##......###...###

...## => #
..#.. => #
.#... => #
.#.#. => #
.#.## => #
.##.. => #
.#### => #
#.#.# => #
#.### => #
##.#. => #
##.## => #
###.. => #
###.# => #
####. => #")

(deftest part1-example
  (is (= 325 (d/part1 example))))

(def answers (delay (day-answers 2018 12)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
