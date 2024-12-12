(ns aoc2024.day07-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day07 :as d]
            [clojure.test :refer [deftest is]]))

(def example-input
  "190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20")

(deftest part1-test-example
  (is (= 3749 (d/part1 example-input))))

(deftest part2-test-example
  (is (= 11387 (d/part2 example-input))))

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2024 7)]
    (is (= answer1 (d/part1 d/input)))
    (is (= answer2 (d/part2 d/input)))))
