(ns aoc.util.pos-test
  (:require
   [aoc.util.pos :as p]
   [clojure.test :refer [deftest is testing]]))

(deftest pos+-test
  (testing "unary"
    (is (= [1 2] (p/pos+ [1 2]))))
  (testing "binary"
    (is (= [4 6] (p/pos+ [1 2] [3 4]))))
  (testing "variadic"
    (is (= [6 9] (p/pos+ [1 2] [2 3] [3 4])))))

(deftest pos--test
  (testing "unary (negation)"
    (is (= [-1 -2] (p/pos- [1 2]))))
  (testing "binary"
    (is (= [-2 -2] (p/pos- [1 2] [3 4]))))
  (testing "variadic"
    (is (= [-4 -5] (p/pos- [1 2] [2 3] [3 4])))))

(deftest pos*n-test
  (is (= [3 6] (p/pos*n 3 [1 2])))
  (is (= [0 0] (p/pos*n 0 [1 2])))
  (is (= [-2 -4] (p/pos*n -2 [1 2]))))

(deftest orthogonal-test
  (testing "dirs"
    (is (= [p/dir-up p/dir-right p/dir-down p/dir-left]
           p/orthogonal-dirs)))
  (testing "neighbors"
    (let [p [10 10]
          neighbors (set (p/orthogonal-to p))]
      (is (= 4 (count neighbors)))
      (is (contains? neighbors [10 9]))  ;; up
      (is (contains? neighbors [11 10])) ;; right
      (is (contains? neighbors [10 11])) ;; down
      (is (contains? neighbors [9 10]))))) ;; left

(deftest diagonal-test
  (testing "dirs"
    (is (= [p/dir-ne p/dir-se p/dir-sw p/dir-nw]
           p/diagonal-dirs)))
  (testing "neighbors"
    (let [p [10 10]
          neighbors (set (p/diagonal-to p))]
      (is (= 4 (count neighbors)))
      (is (contains? neighbors [11 9]))  ;; ne
      (is (contains? neighbors [11 11])) ;; se
      (is (contains? neighbors [9 11]))  ;; sw
      (is (contains? neighbors [9 9]))))) ;; nw

(deftest adjacent-test
  (testing "dirs"
    (is (= 8 (count p/adjacent-dirs)))
    (is (= (set (concat p/orthogonal-dirs p/diagonal-dirs))
           (set p/adjacent-dirs))))
  (testing "neighbors"
    (let [p [10 10]
          neighbors (set (p/adjacent-to p))]
      (is (= 8 (count neighbors)))
      (is (= (set (concat (p/orthogonal-to p) (p/diagonal-to p)))
             neighbors)))))

(deftest opposite-dir-test
  (is (= p/dir-s  (p/opposite-dir p/dir-n)))
  (is (= p/dir-w  (p/opposite-dir p/dir-e)))
  (is (= p/dir-ne (p/opposite-dir p/dir-sw))))

(deftest turns-test
  (testing "turn-left"
    (is (= p/dir-left  (p/turn-left p/dir-up)))
    (is (= p/dir-down  (p/turn-left p/dir-left)))
    (is (= p/dir-right (p/turn-left p/dir-down)))
    (is (= p/dir-up    (p/turn-left p/dir-right))))

  (testing "turn-right"
    (is (= p/dir-right (p/turn-right p/dir-up)))
    (is (= p/dir-down  (p/turn-right p/dir-right)))
    (is (= p/dir-left  (p/turn-right p/dir-down)))
    (is (= p/dir-up    (p/turn-right p/dir-left)))))
