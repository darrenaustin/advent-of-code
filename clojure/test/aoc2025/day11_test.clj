;; Test for aoc2025.day11
(ns aoc2025.day11-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2025.day11 :as d]
            [clojure.test :refer :all]))

(def example1
  "aaa: you hhh
you: bbb ccc
bbb: ddd eee
ccc: ddd eee fff
ddd: ggg
eee: out
fff: out
ggg: out
hhh: ccc fff iii
iii: out")

(def example2
  "svr: aaa bbb
aaa: fft
fft: ccc
bbb: tty
tty: ccc
ccc: ddd eee
ddd: hub
hub: fff
eee: dac
dac: fff
fff: ggg hhh
ggg: out
hhh: out")

(deftest part1-example
  (is (= 5 (d/part1 example1))))

(deftest part2-example
  (is (= 2 (d/part2 example2))))

(def answers (delay (day-answers 2025 11)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
