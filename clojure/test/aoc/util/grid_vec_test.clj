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

(deftest metadata-test
  (let [grid (g/make-grid-vec 2 2)
        m {:a 1}
        grid-with-meta (with-meta grid m)]
    (is (= m (meta grid-with-meta)))
    (is (= grid grid-with-meta) "Metadata should not affect equality")
    (is (= m (meta (assoc grid-with-meta [0 0] :val))) "Metadata should be preserved after assoc")))

(deftest transient-test
  (let [grid (g/make-grid-vec 2 2 0)]
    (testing "transient assoc!"
      (let [t (transient grid)
            _ (assoc! t [0 0] 1)
            _ (assoc! t [1 1] 2)
            p (persistent! t)]
        (is (= 1 (get p [0 0])))
        (is (= 2 (get p [1 1])))
        (is (= 0 (get p [0 1])) "Unchanged cells should remain default")))

    (testing "transient conj!"
      (let [t (transient grid)
            _ (conj! t [[0 0] 5])
            _ (conj! t {[1 0] 6 [0 1] 7})
            p (persistent! t)]
        (is (= 5 (get p [0 0])))
        (is (= 6 (get p [1 0])))
        (is (= 7 (get p [0 1])))))

    (testing "transient count"
      (is (= 4 (count (transient grid)))))

    (testing "transient lookup"
      (let [t (transient grid)
            _ (assoc! t [0 0] :x)]
        (is (= :x (get t [0 0])))
        (is (= 0 (get t [1 1])))))

    (testing "transient dissoc! is a no-op"
      (let [t (transient grid)
            _ (assoc! t [0 0] :x)
            _ (dissoc! t [0 0])]
        (is (= :x (get t [0 0])) "dissoc! should be a no-op")))))

(deftest transformation-test
  (let [grid (g/rows->grid-vec ["ab" "cd"])]
    (testing "rotate-clockwise"
      (let [rotated (g/rotate-clockwise grid)]
        (is (= 2 (g/width rotated)))
        (is (= 2 (g/height rotated)))
        (is (= \c (get rotated [0 0])))
        (is (= \a (get rotated [1 0])))
        (is (= \d (get rotated [0 1])))
        (is (= \b (get rotated [1 1])))))

    (testing "flip-horizontal"
      (let [flipped (g/flip-horizontal grid)]
        (is (= \b (get flipped [0 0])))
        (is (= \a (get flipped [1 0])))
        (is (= \d (get flipped [0 1])))
        (is (= \c (get flipped [1 1])))))

    (testing "flip-vertical"
      (let [flipped (g/flip-vertical grid)]
        (is (= \c (get flipped [0 0])))
        (is (= \d (get flipped [1 0])))
        (is (= \a (get flipped [0 1])))
        (is (= \b (get flipped [1 1])))))))

(deftest sub-grid-test
  (let [grid (g/rows->grid-vec ["abc" "def" "ghi"])]
    (testing "sub-grid extraction"
      (let [sub (g/sub-grid grid [1 1] [2 2])]
        (is (= 2 (g/width sub)))
        (is (= 2 (g/height sub)))
        (is (= \e (get sub [0 0])))
        (is (= \f (get sub [1 0])))
        (is (= \h (get sub [0 1])))
        (is (= \i (get sub [1 1])))))

    (testing "set-sub-grid"
      (let [sub (g/rows->grid-vec ["XY" "Z!"])
            updated (g/set-sub-grid grid [1 1] sub)]
        (is (= \a (get updated [0 0])))
        (is (= \X (get updated [1 1])))
        (is (= \Y (get updated [2 1])))
        (is (= \Z (get updated [1 2])))
        (is (= \! (get updated [2 2])))))

    (testing "set-sub-grid clipping"
      (let [sub (g/rows->grid-vec ["XY" "Z!"])
            updated (g/set-sub-grid grid [2 2] sub)]
        (is (= \X (get updated [2 2])))
        (is (= 3 (g/width updated)))
        (is (= 3 (g/height updated)))))))

(deftest row-col-access-test
  (let [grid (g/rows->grid-vec ["abc" "def" "ghi"])]
    (testing "row access"
      (is (= [\a \b \c] (g/row grid 0)))
      (is (= [\d \e \f] (g/row grid 1)))
      (is (= [\g \h \i] (g/row grid 2))))

    (testing "column access"
      (is (= [\a \d \g] (g/column grid 0)))
      (is (= [\b \e \h] (g/column grid 1)))
      (is (= [\c \f \i] (g/column grid 2))))

    (testing "convenience accessors"
      (is (= [\a \b \c] (g/top-row grid)))
      (is (= [\g \h \i] (g/bottom-row grid)))
      (is (= [\a \d \g] (g/left-column grid)))
      (is (= [\c \f \i] (g/right-column grid))))))
