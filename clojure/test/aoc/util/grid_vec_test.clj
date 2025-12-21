(ns aoc.util.grid-vec-test
  (:require
   [aoc.util.grid-vec :as g]
   [clojure.string :as str]
   [clojure.test :refer [deftest is testing]]))

(deftest grid-creation-test
  (testing "make-grid-vec"
    (let [grid (g/make-grid-vec 3 2 :init)]
      (is (= 3 (g/width grid)))
      (is (= 2 (g/height grid)))
      (is (= :init (get grid [0 0])))
      (is (= :init (get grid [2 1])))))

  (testing "rows->grid-vec with strings"
    (let [grid (g/rows->grid-vec ["abc" "def"])]
      (is (= 3 (g/width grid)))
      (is (= 2 (g/height grid)))
      (is (= \a (get grid [0 0])))
      (is (= \f (get grid [2 1])))))

  (testing "rows->grid-vec with vectors"
    (let [grid (g/rows->grid-vec [[1 2] [3 4]])]
      (is (= 2 (g/width grid)))
      (is (= 2 (g/height grid)))
      (is (= 1 (get grid [0 0])))
      (is (= 4 (get grid [1 1])))))

  (testing "rows->grid-vec with value-fn"
    (let [grid (g/rows->grid-vec ["12" "34"] #(Long/parseLong (str %)))]
      (is (= 1 (get grid [0 0])))
      (is (= 4 (get grid [1 1]))))))

(deftest bounded-protocol-test
  (let [grid (g/rows->grid-vec ["abc" "def"])]
    (is (= 3 (g/width grid)))
    (is (= 2 (g/height grid)))
    (is (= [[0 0] [2 1]] (g/bounds grid)))
    (is (= [0 0] (g/top-left grid)))
    (is (= [2 0] (g/top-right grid)))
    (is (= [2 1] (g/bottom-right grid)))
    (is (= [0 1] (g/bottom-left grid)))
    (is (= [[0 0] [2 0] [2 1] [0 1]] (g/corners grid)))))

(deftest associative-test
  (let [grid (g/rows->grid-vec ["abc" "def"])]
    (testing "get / valAt"
      (is (= \a (get grid [0 0])))
      (is (= \e (get grid [1 1])))
      (is (nil? (get grid [3 0])))
      (is (= :default (get grid [3 0] :default))))

    (testing "contains?"
      (is (true? (contains? grid [0 0])))
      (is (true? (contains? grid [2 1])))
      (is (false? (contains? grid [3 0])))
      (is (false? (contains? grid [-1 0]))))

    (testing "assoc"
      (let [g2 (assoc grid [0 0] \X)]
        (is (= \X (get g2 [0 0])))
        (is (= \a (get grid [0 0])) "Original grid should be immutable")
        (is (= \b (get g2 [1 0])) "Other cells should remain unchanged")))))

(deftest counted-test
  (let [grid (g/rows->grid-vec ["abc" "def"])]
    (is (= 6 (count grid)))))

(deftest seqable-test
  (let [grid (g/rows->grid-vec ["ab" "cd"])]
    (is (= [[[0 0] \a] [[1 0] \b] [[0 1] \c] [[1 1] \d]]
           (seq grid)))))

(deftest ifn-test
  (let [grid (g/rows->grid-vec ["abc"])]
    (is (= \a (grid [0 0])))
    (is (= :default (grid [5 5] :default)))))

(deftest equality-test
  (let [g1 (g/rows->grid-vec ["abc"])
        g2 (g/rows->grid-vec ["abc"])
        g3 (g/rows->grid-vec ["abd"])]
    (is (= g1 g2))
    (is (not= g1 g3))
    (is (= (hash g1) (hash g2)))))

(deftest update-grid-test
  (let [grid (g/rows->grid-vec ["12" "34"] #(Long/parseLong (str %)))
        g2 (g/update-grid grid (fn [[_ v]] (inc v)))]
    (is (= 2 (get g2 [0 0])))
    (is (= 5 (get g2 [1 1])))))

(deftest format-grid-test
  (let [grid (g/rows->grid-vec ["ab" "cd"])]
    (is (= "ab\ncd" (g/format-grid grid)))
    (is (= "a|b\nc|d" (g/format-grid grid :col-sep "|")))
    (is (= "A B\nC D" (g/format-grid grid :col-sep " " :value-fn #(str/upper-case (str %)))))))

(deftest collection-test
  (let [grid (g/rows->grid-vec ["ab" "cd"])]
    (testing "empty"
      (let [e (empty grid)]
        (is (= 0 (count e)))
        (is (= 0 (g/width e)))
        (is (= 0 (g/height e)))))

    (testing "cons"
      (let [g2 (conj grid [[0 0] \X])]
        (is (= \X (get g2 [0 0]))))
      (let [g3 (into grid {[0 0] \Y [1 1] \Z})]
        (is (= \Y (get g3 [0 0])))
        (is (= \Z (get g3 [1 1])))))))
