(ns aoc.util.lp-solver-test
  (:require
   [aoc.util.lp-solver :as lp]
   [clojure.test :refer :all]))

(defn approx= [a b]
  (if (vector? a)
    (and (vector? b)
         (= (count a) (count b))
         (every? true? (map approx= a b)))
    (< (Math/abs (- (double a) (double b))) 1e-6)))

(deftest solve-lp-test
  (testing "simple maximization problem"
    ;; Maximize 3x + 5y
    ;; Subject to:
    ;; x + y <= 4
    ;; x + 3y <= 6
    ;; x >= 0, y >= 0
    (let [result (lp/solve-lp [3 5]
                              [[[1 1] :leq 4]
                               [[1 3] :leq 6]]
                              :maximize)]
      ;; Optimal solution is at (3, 1) with value 14
      (is (approx= 14.0 (:value result)))
      (is (approx= [3.0 1.0] (:point result)))))

  (testing "simple minimization problem"
    ;; Minimize 2x + 3y
    ;; Subject to:
    ;; x + y >= 10
    ;; x >= 0, y >= 0
    (let [result (lp/solve-lp [2 3]
                              [[[1 1] :geq 10]]
                              :minimize)]
      ;; Optimal solution is at (10, 0) with value 20
      (is (approx= 20.0 (:value result)))
      (is (approx= [10.0 0.0] (:point result)))))

  (testing "equality constraints"
    ;; Maximize x + y
    ;; Subject to:
    ;; 2x + y = 10
    ;; x >= 0, y >= 0
    (let [result (lp/solve-lp [1 1]
                              [[[2 1] :eq 10]]
                              :maximize)]
      ;; Optimal solution is at (0, 10) with value 10
      (is (approx= 10.0 (:value result)))
      (is (approx= [0.0 10.0] (:point result))))))

(deftest solve-ilp-test
  (testing "simple ILP maximization"
    ;; Maximize x + y
    ;; Subject to:
    ;; 2x + 2y <= 9
    ;; x >= 0, y >= 0
    ;; LP solution would be 4.5 (e.g. x=4.5, y=0)
    ;; ILP solution should be 4 (e.g. x=4, y=0 or x=0, y=4)
    (let [result (lp/solve-ilp [1 1]
                               [[[2 2] :leq 9]]
                               :maximize)]
      (is (= 4 (:value result)))
      (is (every? integer? (:point result)))
      (is (= 4 (reduce + (:point result))))))

  (testing "ILP with specific integer solution"
    ;; Maximize 5x + 4y
    ;; Subject to:
    ;; x + y <= 5
    ;; 10x + 6y <= 45
    ;; x, y >= 0 integers
    ;; LP optimum: x=3.75, y=1.25, val=23.75
    ;; ILP optimum: x=3, y=2, val=23
    (let [result (lp/solve-ilp [5 4]
                               [[[1 1] :leq 5]
                                [[10 6] :leq 45]]
                               :maximize)]
      (is (= 23 (:value result)))
      (is (= [3 2] (:point result)))))

  (testing "ILP minimization"
    ;; Minimize x + y
    ;; Subject to:
    ;; 2x + 2y >= 9
    ;; x, y >= 0 integers
    ;; LP optimum: 4.5
    ;; ILP optimum: 5 (e.g. x=3, y=2 is 5? No 2*3+2*2 = 10 >= 9. x=2, y=2 is 8 < 9.
    ;; x=5, y=0 -> 10 >= 9. sum=5.
    ;; x=0, y=5 -> 10 >= 9. sum=5.
    ;; x=2, y=3 -> 4+6=10 >= 9. sum=5.
    ;; x=1, y=4 -> 2+8=10 >= 9. sum=5.
    ;; x=3, y=2 -> 6+4=10 >= 9. sum=5.
    ;; Is there a sum=4?
    ;; x=4, y=0 -> 8 < 9.
    ;; x=2, y=2 -> 8 < 9.
    ;; So min sum is 5.
    (let [result (lp/solve-ilp [1 1]
                               [[[2 2] :geq 9]]
                               :minimize)]
      (is (= 5 (:value result)))
      (is (every? integer? (:point result)))
      (is (= 5 (reduce + (:point result)))))))
