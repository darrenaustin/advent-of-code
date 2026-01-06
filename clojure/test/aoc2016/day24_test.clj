;; Test for aoc2016.day24
(ns aoc2016.day24-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day24 :as d]
   [clojure.test :refer :all]))

(def example
  "###########
#0.1.....2#
#.#######.#
#4.......3#
###########")

(deftest part1-example
  (is (= 14 (d/part1 example))))

(deftest part2-example
  (is (= 20 (d/part2 example))))

(def answers (delay (day-answers 2016 24)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
