(ns aoc.util.grid-test
  (:require
    [aoc.util.grid :refer :all]
    [clojure.test :refer :all]))

(deftest diamond-around-test
  (is (= #{[0 0]} (diamond-around 0 0 [0 0])))
  (is (= #{[0 0] [0 1] [0 -1] [1 0] [-1 0]} (diamond-around 0 1 [0 0])))
  (is (= #{[0 1] [0 -1] [1 0] [-1 0]} (diamond-around 1 1 [0 0])))
  (is (= #{[0 2] [0 -2] [2 0] [-2 0] [1 1] [1 -1] [-1 1] [-1 -1]} (diamond-around 2 2 [0 0]))))
