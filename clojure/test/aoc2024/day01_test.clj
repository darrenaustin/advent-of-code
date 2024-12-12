(ns aoc2024.day01-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day01 :as d]
            [clojure.test :refer [deftest is]]))

(def example-input
  "3   4
4   3
2   5
1   3
3   9
3   3")

(deftest part1-test-example
  (is (= 11 (d/part1 example-input))))

(deftest part2-test-example
  (is (= 31 (d/part2 example-input))))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2024 1)]
    (is (= answer1 (d/part1 d/input)))
    (is (= answer2 (d/part2 d/input)))))
