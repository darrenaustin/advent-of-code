;; Test for aoc2016.day23
(ns aoc2016.day23-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day23 :as d]
   [clojure.test :refer :all]))

(def example
  "cpy 2 a
tgl a
tgl a
tgl a
cpy 1 a
dec a
dec a")

(deftest part1-example
  (is (= 3 (d/part1 example))))

(deftest part2-example
  (is (= 3 (d/part2 example))))

(def answers (delay (day-answers 2016 23)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
