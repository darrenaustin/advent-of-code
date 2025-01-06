;; Test for aoc2018.day9
(ns aoc2018.day09-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2018.day09 :as d]
            [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
    (= expected (d/part1 input))
    32 "9 players; last marble is worth 25 points"
    8317 "10 players; last marble is worth 1618 points"
    146373 "13 players; last marble is worth 7999 points"
    2764 "17 players; last marble is worth 1104 points"
    54718 "21 players; last marble is worth 6111 points"
    37305 "30 players; last marble is worth 5807 points"))

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2018 9) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
