;; Test for aoc2015.day7
(ns aoc2015.day07-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2015.day07 :as d]
   [clojure.test :refer :all]))

(def example
  "123 -> x
456 -> y
x AND y -> d
x OR y -> e
x LSHIFT 2 -> f
y RSHIFT 2 -> g
NOT x -> h
NOT y -> i")

(deftest simulate-test
  (let [circuit (d/parse-circuit example)]
    (are [expected input]
         (= expected ((d/simulate circuit) input))
      72    "d"
      507   "e"
      492   "f"
      114   "g"
      65412 "h"
      65079 "i"
      123   "x"
      456   "y")))

(def answers (delay (day-answers 2015 7)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
