;; Test for aoc2017.day7
(ns aoc2017.day07-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2017.day07 :as d]
   [clojure.test :refer :all]))

(def example
  "pbga (66)
xhth (57)
ebii (61)
havc (66)
ktlj (57)
fwft (72) -> ktlj, cntj, xhth
qoyq (66)
padx (45) -> pbga, havc, qoyq
tknk (41) -> ugml, padx, fwft
jptl (61)
ugml (68) -> gyxo, ebii, jptl
gyxo (61)
cntj (57)")

(deftest part1-example
  (is (= "tknk" (d/part1 example))))

(deftest part2-example
  (is (= 60 (d/part2 example))))

(def answers (delay (day-answers 2017 7)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
