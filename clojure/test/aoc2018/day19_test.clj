;; Test for aoc2018.day19
(ns aoc2018.day19-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2018.day19 :as d]
            [clojure.test :refer :all]))

(def example
  "#ip 0
seti 5 0 1
seti 6 0 2
addi 0 1 0
addr 1 2 3
setr 1 0 0
seti 8 0 4
seti 9 0 5")

(deftest part1-example
  (is (= 6 (d/part1 example))))

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2018 19) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
