;; Test for aoc2017.day18
(ns aoc2017.day18-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2017.day18 :as d]
   [clojure.test :refer :all]))

(def example
  "set a 1
add a 2
mul a a
mod a 5
snd a
set a 0
rcv a
jgz a -1
set a 1
jgz a -2")

(deftest part1-example
  (is (= 4 (d/part1 example))))

(def answers (delay (day-answers 2017 18)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
