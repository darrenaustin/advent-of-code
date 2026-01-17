;; Test for aoc2020.day9
(ns aoc2020.day09-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc.util.string :as s]
   [aoc2020.day09 :as d]
   [clojure.test :refer :all]))

(def example
  "35
20
15
25
47
40
62
55
65
95
102
117
150
182
127
219
299
277
309
576")

(deftest first-invalid-example
  (is (= 127 (d/first-invalid (s/ints example) 5))))

(deftest encryption-weakness-example
  (is (= 62 (d/encryption-weakness (s/ints example) 5))))

(def answers (delay (day-answers 2020 9)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
