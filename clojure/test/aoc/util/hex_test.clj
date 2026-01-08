(ns aoc.util.hex-test
  (:require
   [aoc.util.hex :as hex]
   [clojure.test :refer [deftest is testing]]))

(deftest dirs-test
  (testing "All directions satisfy the cube coordinate invariant (x + y + z = 0)"
    (doseq [[dir vec] hex/dirs]
      (is (zero? (reduce + vec)) (str dir " should sum to 0")))))

(deftest distance-test
  (testing "Distance from origin"
    (is (= 0 (hex/distance [0 0 0])))
    (is (= 1 (hex/distance [1 -1 0])))
    (is (= 2 (hex/distance [2 -2 0]))))

  (testing "Distance between points"
    (is (= 0 (hex/distance [1 -1 0] [1 -1 0])))
    (is (= 1 (hex/distance [0 0 0] [1 -1 0])))
    (is (= 2 (hex/distance [0 0 0] [2 -2 0])))
    (is (= 2 (hex/distance [1 -1 0] [-1 1 0])))))
