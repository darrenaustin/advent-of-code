(ns aoc.util.grid-test
  (:require
    [aoc.util.grid :refer :all]
    [clojure.test :refer :all]))

(deftest diamond-around-test
  (is (= [[0 0]] (diamond-around 0 0 [0 0])))
  (is (= (sort [[0 0] [0 1] [0 -1] [1 0] [-1 0]])
         (sort (diamond-around 0 1 [0 0]))))
  (is (= (sort [[0 1] [0 -1] [1 0] [-1 0]])
         (sort (diamond-around 1 1 [0 0]))))
  (is (= (sort [[0 2] [0 -2] [2 0] [-2 0] [1 1] [1 -1] [-1 1] [-1 -1]])
         (sort (diamond-around 2 2 [0 0])))))
