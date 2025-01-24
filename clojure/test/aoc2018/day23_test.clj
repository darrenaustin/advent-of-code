;; Test for aoc2018.day23
(ns aoc2018.day23-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2018.day23 :as d]
            [clojure.test :refer :all]))

(def example1
  "pos=<0,0,0>, r=4
pos=<1,0,0>, r=1
pos=<4,0,0>, r=3
pos=<0,2,0>, r=1
pos=<0,5,0>, r=3
pos=<0,0,3>, r=1
pos=<1,1,1>, r=1
pos=<1,1,2>, r=1
pos=<1,3,1>, r=1")

(def example2
  "pos=<10,12,12>, r=2
pos=<12,14,12>, r=2
pos=<16,12,12>, r=4
pos=<14,14,14>, r=6
pos=<50,50,50>, r=200
pos=<10,10,10>, r=5")

(deftest part1-example
  (is (= 7 (d/part1 example1))))

(deftest part2-example
  (is (= 36 (d/part2 example2))))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2018 23) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
