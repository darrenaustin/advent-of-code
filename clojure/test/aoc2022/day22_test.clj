;; Test for aoc2022.day22
(ns aoc2022.day22-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2022.day22 :as d]
   [clojure.string :as str]
   [clojure.test :refer :all]))

(def example
  (str/join "\n"
            ["        ...#    "
             "        .#..    "
             "        #...    "
             "        ....    "
             "...#.......#    "
             "........#...    "
             "..#....#....    "
             "..........#.    "
             "        ...#...."
             "        .....#.."
             "        .#......"
             "        ......#."
             ""
             "10R5L5R10L4R5L5"]))

(deftest part1-example
  (is (= 6032 (d/part1 example))))

(def answers (delay (day-answers 2022 22)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
