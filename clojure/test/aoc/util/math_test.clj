(ns aoc.util.math-test
  (:require
   [aoc.util.math :as m]
   [clojure.test :refer :all]))

(deftest sign-test
  (testing "sign returns -1, 0, or 1 based on the sign of the number"
    (is (= -1 (m/sign -5)))
    (is (= -1 (m/sign -0.1)))
    (is (= 0 (m/sign 0)))
    (is (= 0 (m/sign 0.0)))
    (is (= 1 (m/sign 5)))
    (is (= 1 (m/sign 0.1)))))

(deftest clamp-test
  (testing "clamp constrains a number between a lower and upper bound"
    (is (= 5 (m/clamp 0 5 10)))
    (is (= 10 (m/clamp 15 5 10)))
    (is (= 7 (m/clamp 7 5 10)))
    (is (= 5 (m/clamp 5 5 10)))
    (is (= 10 (m/clamp 10 5 10)))
    (is (= -5 (m/clamp -10 -5 5)))
    (is (= 5 (m/clamp 10 -5 5)))
    (is (= 0 (m/clamp 0 -5 5)))))

(deftest intervals-test
  (is (= [1 0 -5 7 1] (m/intervals [1 2 2 -3 4 5])))
  (is (= [2 2 2 2] (m/intervals [0 2 4 6 8])))
  (is (= [1 2 3] (m/intervals [0 1 3 6])))
  (is (= [] (m/intervals [1])))
  (is (= [] (m/intervals [])))
  (is (= [2 0 -2] (m/intervals 2 [1 2 3 2 1])))
  (is (= [1 -1] (m/intervals 3 [1 2 3 2 1]))))

(deftest indexed-min-test
  (is (= [2 1] (m/indexed-min [3 5 1 7 2])))
  (is (= [0 -5] (m/indexed-min [-5 3 1 0])))
  (is (= [0 1] (m/indexed-min [1])))
  (is (= [0 5] (m/indexed-min [5 10 15 20])))
  (is (= [1 2] (m/indexed-min [10 2 5 2 8]))))

(deftest indexed-max-test
  (is (= [3 7] (m/indexed-max [3 5 1 7 2])))
  (is (= [1 3] (m/indexed-max [-5 3 1 0])))
  (is (= [0 1] (m/indexed-max [1])))
  (is (= [3 20] (m/indexed-max [5 10 15 20])))
  (is (= [0 10] (m/indexed-max [10 2 5 2 8]))))

(deftest quadratic-roots-test
  (is (= #{5} (set (m/quadratic-roots 0 1 -5))))
  (is (= #{-7.0 7.0} (set (m/quadratic-roots -1 0 49))))
  (is (= #{-6.0 8.0} (set (m/quadratic-roots -1 2 48))))
  (is (= #{-7.0 2.0} (set (m/quadratic-roots 1 5 -14))))
  (is (= #{-7.0 -3.0} (set (m/quadratic-roots 1 10 21))))
  (is (= #{-1} (set (m/quadratic-roots 0 5 5))))
  (is (= #{-3.618033988749895 -1.381966011250105} (set (m/quadratic-roots 1 5 5))))
  (is (= #{} (set (m/quadratic-roots 9  5  5))))
  (is (= #{} (set (m/quadratic-roots 10 5 5))))
  (is (= #{-1} (set (m/quadratic-roots 5 10 5))))
  (is (= #{} (set (m/quadratic-roots 0 0 5))))
  (is (= :any (m/quadratic-roots 0 0 0))))

(deftest mod-inverse-test
  (are [expected a m]
       (= expected (m/mod-inverse a m))
    1969 42 2017
    0 40 1
    96 52 -217
    121 -486 217
    nil 40 2018))

(deftest chinese-remainder-test
  (is (= 23 (m/chinese-remainder [2 3 2] [3 5 7])))
  (is (= 33 (m/chinese-remainder [3 5] [5 7])))
  (is (= 1 (m/chinese-remainder [1] [5])))
  (is (= 0 (m/chinese-remainder [0] [5])))
  (is (= 39 (m/chinese-remainder [0 3 4] [3 4 5]))))
