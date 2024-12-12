(ns aoc2018.day06-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2018.day06 :as d]
            [clojure.test :refer [deftest is]]))

(def example-input
  "1, 1
1, 6
8, 3
3, 4
5, 5
8, 9")

(deftest part1-example
  (is (= 17 (d/part1 example-input))))

(deftest part2-test-example
  (is (= 16 (d/part2 example-input 32))))

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2018 6)]
    (is (= answer1 (d/part1 d/input)))
    (is (= answer2 (d/part2 d/input)))))
