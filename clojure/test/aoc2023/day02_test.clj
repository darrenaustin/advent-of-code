;; Test for aoc2023.day2
(ns aoc2023.day02-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2023.day02 :as d]
   [clojure.test :refer :all]))

(def example
  "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green")

(deftest part1-example
  (is (= 8 (d/part1 example))))

(deftest part2-example
  (is (= 2286 (d/part2 example))))

(def answers (delay (day-answers 2023 2)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
