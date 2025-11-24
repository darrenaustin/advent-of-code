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

(def answers (delay (day-answers 2018 9)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
