;; Test for aoc2017.day20
(ns aoc2017.day20-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2017.day20 :as d]
            [clojure.test :refer :all]))

(def example1
  "p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>
p=< 4,0,0>, v=< 0,0,0>, a=<-2,0,0>")

(def example2
  "p=<-6,0,0>, v=< 3,0,0>, a=< 0,0,0>
p=<-4,0,0>, v=< 2,0,0>, a=< 0,0,0>
p=<-2,0,0>, v=< 1,0,0>, a=< 0,0,0>
p=< 3,0,0>, v=<-1,0,0>, a=< 0,0,0>")

(deftest part1-example
  (is (zero? (d/part1 example1))))

(deftest part2-example
  (is (= 1 (d/part2 example2))))

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2017 20) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
