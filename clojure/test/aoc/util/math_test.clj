(ns aoc.util.math-test
  (:require [aoc.util.math :refer [quadratic-roots]]
            [clojure.test :refer :all]))

(defn num-set-equiv [ns1 ns2]
  (and (= (count ns1) (count ns2))
       (every? identity (map == (sort ns1) (sort ns2)))))

(deftest quadratic-roots-test
  (is (num-set-equiv [5] (quadratic-roots 0 1 -5)))
  (is (num-set-equiv [-7 7] (quadratic-roots -1 0 49)))
  (is (num-set-equiv [-6 8] (quadratic-roots -1 2 48)))
  (is (num-set-equiv [-7 2] (quadratic-roots 1 5 -14)))
  (is (num-set-equiv [-7 -3] (quadratic-roots 1 10 21)))
  (is (num-set-equiv [-1] (quadratic-roots 0 5 5)))
  (is (num-set-equiv [-3.618033988749895 -1.381966011250105] (quadratic-roots 1 5 5)))
  (is (num-set-equiv [] (quadratic-roots 9	5	5)))
  (is (num-set-equiv [] (quadratic-roots 10 5 5)))
  (is (num-set-equiv [-1] (quadratic-roots 5 10 5)))
  (is (num-set-equiv [] (quadratic-roots 0 0 5)))
  (is (= :any (quadratic-roots 0 0 0))))
