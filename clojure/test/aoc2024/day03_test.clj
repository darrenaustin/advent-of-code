(ns aoc2024.day03-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day03 :as d]
            [clojure.test :refer [deftest is]]))

(deftest part1-example
  (is (= 161 (d/part1 "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"))))

(deftest part2-example
  (is (= 48 (d/part2 "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"))))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2024 3)]
    (is (= answer1 (d/part1 d/input)))
    (is (= answer2 (d/part2 d/input)))))
