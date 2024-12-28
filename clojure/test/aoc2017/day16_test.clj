;; Test for aoc2017.day16
(ns aoc2017.day16-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2017.day16 :as d]
            [clojure.test :refer :all]))

(def example "s1,x3/4,pe/b")

(deftest part1-example
  (is (= "baedc" (d/part1 example "abcde"))))

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2017 16) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
