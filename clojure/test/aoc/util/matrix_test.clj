(ns aoc.util.matrix-test
  (:require
   [aoc.util.matrix :as mat]
   [clojure.test :refer :all]))

(deftest transpose-test
  (testing "transpose swaps rows and columns"
    (is (= [[1 3] [2 4]]
           (mat/transpose [[1 2] [3 4]])))
    (is (= [[1 4 7] [2 5 8] [3 6 9]]
           (mat/transpose [[1 2 3] [4 5 6] [7 8 9]])))
    (is (= [[1] [2] [3]]
           (mat/transpose [[1 2 3]]))))

  (testing "jagged transpose with padding"
    (is (= [[1 4] [2 nil] [3 nil]] (mat/transpose [[1 2 3] [4]] nil)))
    (is (= [[1 4] [2 0] [3 0]] (mat/transpose [[1 2 3] [4]] 0)))
    (is (= [[1 4] [2 5] [3 0]] (mat/transpose [[1 2 3] [4 5]] 0)))))

(deftest flip-horizontal-test
  (testing "flip-horizontal reverses each row"
    (is (= [[2 1] [4 3]]
           (mat/flip-horizontal [[1 2] [3 4]])))
    (is (= [[3 2 1] [6 5 4]]
           (mat/flip-horizontal [[1 2 3] [4 5 6]])))
    (is (= [] (mat/flip-horizontal [])))
    (is (= [[1]] (mat/flip-horizontal [[1]])))))

(deftest rotate-right-test
  (testing "rotate-right rotates 90 degrees clockwise"
    (is (= [[3 1] [4 2]]
           (mat/rotate-right [[1 2] [3 4]])))
    (is (= [[7 4 1] [8 5 2] [9 6 3]]
           (mat/rotate-right [[1 2 3] [4 5 6] [7 8 9]])))
    (is (= [[1]] (mat/rotate-right [[1]])))))
