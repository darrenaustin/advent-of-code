;; Test for aoc2025.day6
(ns aoc2025.day06-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2025.day06 :as d]
   [clojure.string :as str]
   [clojure.test :refer :all]))

(def example (str/join "\n"
                       ["123 328  51 64 "
                        " 45 64  387 23 "
                        "  6 98  215 314"
                        "*   +   *   +  "]))

(deftest part1-example
  (is (= 4277556 (d/part1 example))))

(deftest part2-example
  (is (= 3263827 (d/part2 example))))

(def answers (delay (day-answers 2025 6)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
