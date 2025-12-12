(ns aoc.util.grid-test
  (:require
   [aoc.util.grid :as g]
   [clojure.test :refer :all]))

(deftest diamond-around-test
  (is (= [[0 0]] (g/diamond-around 0 0 [0 0])))
  (is (= (sort [[0 0] [0 1] [0 -1] [1 0] [-1 0]])
         (sort (g/diamond-around 0 1 [0 0]))))
  (is (= (sort [[0 1] [0 -1] [1 0] [-1 0]])
         (sort (g/diamond-around 1 1 [0 0]))))
  (is (= (sort [[0 2] [0 -2] [1 1] [1 -1] [-1 1] [-1 -1] [2 0] [-2 0]])
         (sort (g/diamond-around 2 2 [0 0])))))
