;; Test for aoc2020.day23
(ns aoc2020.day23-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2020.day23 :as d]
            [clojure.test :refer :all]))

(deftest part1-example
  (is (= 92658374 (d/part1 "389125467" 10)))
  (is (= 67384529 (d/part1 "389125467"))))

(deftest ^:slow part2-example
  (is (= 149245887792 (d/part2 "389125467"))))

(def answers (delay (day-answers 2020 23)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest ^:slow part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
