;; Test for aoc2025.day12
(ns aoc2025.day12-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2025.day12 :as d]
   [clojure.test :refer :all]))

(def answers (delay (day-answers 2025 12)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
