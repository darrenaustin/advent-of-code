;; Test for aoc2022.day6
(ns aoc2022.day06-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2022.day06 :as d]
   [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    7  "mjqjpqmgbljsphdztnvjfqwrcgsmlb"
    5  "bvwbjplbgvbhsrlpgdmjqwftvncz"
    6  "nppdvjthqldpwncqszvftbrmjlhg"
    10 "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"
    11 "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    19 "mjqjpqmgbljsphdztnvjfqwrcgsmlb"
    23 "bvwbjplbgvbhsrlpgdmjqwftvncz"
    23 "nppdvjthqldpwncqszvftbrmjlhg"
    29 "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"
    26 "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"))

(def answers (delay (day-answers 2022 6)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
