;; Test for aoc2023.day10
(ns aoc2023.day10-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2023.day10 :as d]
   [clojure.test :refer :all]))

(def example1
  "-L|F7
7S-7|
L|7||
-L-J|
L|-JF")

(def example2
  "7-F7-
.FJ|7
SJLL7
|F--J
LJ.LJ")

(def example3
  "...........
.S-------7.
.|F-----7|.
.||.....||.
.||.....||.
.|L-7.F-J|.
.|..|.|..|.
.L--J.L--J.
...........")

(def example4
  ".F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ...")

(def example5
  "FF7FSF7F7F7F7F7F---7
L|LJ||||||||||||F--J
FL-7LJLJ||||||LJL-77
F--JF--7||LJLJ7F7FJ-
L---JF-JLJ.||-FJLJJ7
|F|F-JF---7F7-L7L|7|
|FFJF7L7F-JF7|JL---7
7-L-JL7||F7|L7F-7F7|
L.L7LFJ|||||FJL7||LJ
L7JLJL-JLJLJL--JLJ.L")

(deftest part1-examples
  (are [expected input]
       (= expected (d/part1 input))
    4 example1
    8 example2))

(deftest part2-examples
  (are [expected input]
       (= expected (d/part2 input))
    4 example3
    8 example4
    10 example5))

(def answers (delay (day-answers 2023 10)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
