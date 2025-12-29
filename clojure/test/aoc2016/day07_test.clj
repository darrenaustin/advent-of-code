;; Test for aoc2016.day7
(ns aoc2016.day07-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day07 :as d]
   [clojure.test :refer :all]))

(def example1
  "abba[mnop]qrst
abcd[bddb]xyyx
aaaa[qwer]tyui
ioxxoj[asdfgh]zxcvbn")

(def example2
  "aba[bab]xyz
xyx[xyx]xyx
aaa[kek]eke
zazbz[bzb]cdb")

(deftest part1-example
  (is (= 2 (d/part1 example1))))

(deftest part2-example
  (is (= 3 (d/part2 example2))))

(def answers (delay (day-answers 2016 7)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
