;; Test for aoc2018.day2
(ns aoc2018.day02-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2018.day02 :as d]
   [clojure.test :refer :all]))

(def example1
  "abcdef
bababc
abbcde
abcccd
aabcdd
abcdee
ababab")

(def example2
  "abcde
fghij
klmno
pqrst
fguij
axcye
wvxyz")

(deftest part1-example
  (is (= 12 (d/part1 example1))))

(deftest part2-example
  (is (= "fgij" (d/part2 example2))))

(def answers (delay (day-answers 2018 2)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
