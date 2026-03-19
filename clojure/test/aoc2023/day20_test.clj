;; Test for aoc2023.day20
(ns aoc2023.day20-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2023.day20 :as d]
   [clojure.test :refer :all]))

(def example1
  "broadcaster -> a, b, c
%a -> b
%b -> c
%c -> inv
&inv -> a")

(def example2
  "broadcaster -> a
%a -> inv, con
&inv -> b
%b -> con
&con -> output")

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    32000000 example1
    11687500 example2))

(def answers (delay (day-answers 2023 20)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
