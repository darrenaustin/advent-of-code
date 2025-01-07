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

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2018 12) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
