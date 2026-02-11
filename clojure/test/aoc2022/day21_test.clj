;; Test for aoc2022.day21
(ns aoc2022.day21-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2022.day21 :as d]
   [clojure.test :refer :all]))

(def example
  "root: pppw + sjmn
dbpl: 5
cczh: sllz + lgvd
zczc: 2
ptdq: humn - dvpt
dvpt: 3
lfqf: 4
humn: 5
ljgn: 2
sjmn: drzm * dbpl
sllz: 4
pppw: cczh / lfqf
lgvd: ljgn * ptdq
drzm: hmdt - zczc
hmdt: 32")

(deftest part1-example
  (is (= 152 (d/part1 example))))

(deftest part2-example
  (is (= 301 (d/part2 example))))

(def answers (delay (day-answers 2022 21)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
