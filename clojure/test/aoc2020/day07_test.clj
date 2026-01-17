;; Test for aoc2020.day7
(ns aoc2020.day07-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2020.day07 :as d]
   [clojure.test :refer :all]))

(def example
  "light red bags contain 1 bright white bag, 2 muted yellow bags.
dark orange bags contain 3 bright white bags, 4 muted yellow bags.
bright white bags contain 1 shiny gold bag.
muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
dark olive bags contain 3 faded blue bags, 4 dotted black bags.
vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
faded blue bags contain no other bags.
dotted black bags contain no other bags.")

(deftest part1-example
  (is (= 4 (d/part1 example))))

(deftest part2-example
  (is (= 32 (d/part2 example))))

(def answers (delay (day-answers 2020 7)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
