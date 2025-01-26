(ns aoc2024.day03-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day03 :as d]
            [clojure.test :refer :all]))

(deftest part1-example
  (is (= 161 (d/part1 "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"))))

(deftest part2-example
  (is (= 48 (d/part2 "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"))))

(def answers (delay (day-answers 2024 3)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
