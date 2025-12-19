(ns aoc.util.combo-test
  (:require
   [aoc.util.combo :as combo]
   [clojure.test :refer [deftest is testing]]))

;; A non-memoized helper for testing the logic purely
(defn- test-weak-compositions [n total]
  (combo/weak-compositions-step test-weak-compositions n total))

(deftest weak-compositions-test
  (testing "Base cases"
    (is (= [[0]] (test-weak-compositions 1 0)))
    (is (= [[5]] (test-weak-compositions 1 5))))

  (testing "n=2, total=2"
    (let [result (test-weak-compositions 2 2)]
      (is (= 3 (count result)))
      (is (= #{'(0 2) '(1 1) '(2 0)} (set result)))))

  (testing "n=3, total=2"
    (let [result (test-weak-compositions 3 2)]
      (is (= 6 (count result))) ;; (n+k-1)C(n-1) = 4C2 = 6
      (is (every? #(= 3 (count %)) result))
      (is (every? #(= 2 (apply + %)) result))))

  (testing "Larger example n=3, total=5"
    (let [result (test-weak-compositions 3 5)]
      ;; (5+3-1)C(3-1) = 7C2 = 21
      (is (= 21 (count result)))
      (is (every? #(= 3 (count %)) result))
      (is (every? #(= 5 (apply + %)) result)))))

(deftest weak-compositions-step-test
  (testing "Manual recursion control"
    ;; We just test one level deep manually to prove we can inject logic
    ;; n=2, total=2 -> calls recur with n=1
    (is (= [[0 2] [1 1] [2 0]]
           (combo/weak-compositions-step
            (fn [n t] (combo/weak-compositions-step nil n t))
            2 2)))))
