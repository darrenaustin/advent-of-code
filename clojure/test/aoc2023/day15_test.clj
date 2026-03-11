;; Test for aoc2023.day15
(ns aoc2023.day15-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2023.day15 :as d]
   [clojure.test :refer :all]))

(def example
  "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7")

(deftest part1-example
  (is (= 1320 (d/part1 example))))

(deftest part2-example
  (is (= 145 (d/part2 example))))

(def answers (delay (day-answers 2023 15)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
