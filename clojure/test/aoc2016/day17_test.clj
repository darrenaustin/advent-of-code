;; Test for aoc2016.day17
(ns aoc2016.day17-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2016.day17 :as d]
   [clojure.test :refer :all]))

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    "DDRRRD" "ihgpwlah"
    "DDUDRLRRUDRD" "kglvqrro"
    "DRURDRUDDLLDLUURRDULRLDUUDDDRR" "ulqzkmiv"))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    370 "ihgpwlah"
    492 "kglvqrro"
    830 "ulqzkmiv"))

(def answers (delay (day-answers 2016 17)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
