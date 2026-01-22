;; Test for aoc2022.day7
(ns aoc2022.day07-test
  (:require
   [aoc.day :refer [day-answers]]
   [aoc2022.day07 :as d]
   [clojure.test :refer :all]))

(def example
  "$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k")

(deftest part1-example
  (is (= 95437 (d/part1 example))))

(deftest part2-example
  (is (= 24933642 (d/part2 example))))

(def answers (delay (day-answers 2022 7)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
