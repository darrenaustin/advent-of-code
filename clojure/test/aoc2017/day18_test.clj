;; Test for aoc2017.day18
(ns aoc2017.day18-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2017.day18 :as d]
            [clojure.test :refer :all]))

(def example
  "set a 1
add a 2
mul a a
mod a 5
snd a
set a 0
rcv a
jgz a -1
set a 1
jgz a -2")

(deftest part1-example
  (is (= 4 (d/part1 example))))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2017 18) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
