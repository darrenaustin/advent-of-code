;; Test for aoc2016.day12
(ns aoc2016.day12-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day12 :as d]
   [clojure.test :refer :all]))

(def example
  "cpy 41 a
inc a
inc a
dec a
jnz a 2
dec a")

(deftest part1-example
  (is (= 42 (d/part1 example))))

(def answers (delay (day-answers 2016 12)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
