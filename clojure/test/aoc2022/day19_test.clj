;; Test for aoc2022.day19
(ns aoc2022.day19-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2022.day19 :as d]
   [clojure.test :refer :all]))

(def example
  "Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.")

(deftest part1-example
  (is (= 33 (d/part1 example))))

(def answers (delay (day-answers 2022 19)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
