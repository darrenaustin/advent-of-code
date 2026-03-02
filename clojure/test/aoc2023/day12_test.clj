;; Test for aoc2023.day12
(ns aoc2023.day12-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2023.day12 :as d]
   [clojure.test :refer :all]))

(def example
  "???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1")

(deftest part1-example
  (is (= 21 (d/part1 example))))

(deftest part2-example
  (is (= 525152 (d/part2 example))))

(def answers (delay (day-answers 2023 12)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
