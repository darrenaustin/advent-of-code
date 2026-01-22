;; Test for aoc2022.day5
(ns aoc2022.day05-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2022.day05 :as d]
   [clojure.string :as str]
   [clojure.test :refer :all]))

(def example
  (str/join "\n"
            ["    [D]    "
             "[N] [C]    "
             "[Z] [M] [P]"
             " 1   2   3 "
             ""
             "move 1 from 2 to 1"
             "move 3 from 1 to 3"
             "move 2 from 2 to 1"
             "move 1 from 1 to 2"]))

(deftest part1-example
  (is (= "CMZ" (d/part1 example))))

(deftest part2-example
  (is (= "MCD" (d/part2 example))))

(def answers (delay (day-answers 2022 5)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
