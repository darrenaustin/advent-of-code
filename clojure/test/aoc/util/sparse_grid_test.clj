(ns aoc.util.sparse-grid-test
  (:require
   [aoc.util.bounded :as b]
   [aoc.util.sparse-grid :as sg]
   [clojure.string :as str]
   [clojure.test :refer [deftest is testing]]))

(deftest sparse-grid-creation-test
  (testing "make-sparse-grid"
    (let [grid (sg/make-sparse-grid)]
      (is (= 0 (count grid)))
      (is (nil? (b/bounds grid)))
      (is (= 0 (b/width grid)))
      (is (= 0 (b/height grid)))))

  (testing "map->sparse-grid"
    (let [m {[0 0] :a [2 2] :b}
          grid (sg/map->sparse-grid m)]
      (is (= 2 (count grid)))
      (is (= [[0 0] [2 2]] (b/bounds grid)))
      (is (= 3 (b/width grid)))
      (is (= 3 (b/height grid)))
      (is (= :a (get grid [0 0])))
      (is (= :b (get grid [2 2]))))))

(deftest associative-test
  (let [grid (sg/make-sparse-grid)]
    (testing "assoc expands bounds"
      (let [g1 (assoc grid [0 0] :a)]
        (is (= [[0 0] [0 0]] (b/bounds g1)))
        (let [g2 (assoc g1 [2 1] :b)]
          (is (= [[0 0] [2 1]] (b/bounds g2)))
          (is (= 3 (b/width g2)))
          (is (= 2 (b/height g2))))))

    (testing "dissoc shrinks bounds"
      (let [grid (sg/map->sparse-grid {[0 0] :a [2 2] :b})
            g2 (dissoc grid [2 2])]
        (is (= [[0 0] [0 0]] (b/bounds g2)))
        (is (= 1 (b/width g2)))
        (is (= 1 (b/height g2))))
      (let [grid (sg/map->sparse-grid {[0 0] :a [2 2] :b})
            g2 (dissoc grid [1 1])] ;; Removing non-existent key
        (is (= [[0 0] [2 2]] (b/bounds g2)))))

    (testing "dissoc edge point (non-corner)"
      (let [grid (sg/map->sparse-grid {[0 0] :a [1 0] :b [2 0] :c [1 1] :d})]
        ;; Bounds are [[0 0] [2 1]]
        (is (= [[0 0] [2 1]] (b/bounds grid)))

        ;; Remove [1 1]. It is at y=1 (max-y). It is the ONLY point at max-y.
        ;; Should trigger recalc and shrink bounds to [[0 0] [2 0]].
        (let [g2 (dissoc grid [1 1])]
          (is (= [[0 0] [2 0]] (b/bounds g2))))

        ;; Remove [1 0]. It is at y=0 (min-y). But [0 0] and [2 0] are also at min-y.
        ;; Should trigger recalc, but bounds remain [[0 0] [2 1]].
        (let [g3 (dissoc grid [1 0])]
          (is (= [[0 0] [2 1]] (b/bounds g3))))))

    (testing "dissoc to empty"
      (let [grid (sg/map->sparse-grid {[0 0] :a})
            g2 (dissoc grid [0 0])]
        (is (nil? (b/bounds g2)))
        (is (= 0 (count g2)))))))

(deftest transient-test
  (testing "transient assoc"
    (let [grid (sg/make-sparse-grid)
          t (transient grid)
          _ (assoc! t [0 0] :a)
          _ (assoc! t [2 2] :b)
          g2 (persistent! t)]
      (is (= 2 (count g2)))
      (is (= [[0 0] [2 2]] (b/bounds g2)))
      (is (= :a (get g2 [0 0])))
      (is (= :b (get g2 [2 2])))))

  (testing "transient dissoc"
    (let [grid (sg/map->sparse-grid {[0 0] :a [2 2] :b})
          t (transient grid)
          _ (dissoc! t [2 2])
          g2 (persistent! t)]
      (is (= 1 (count g2)))
      (is (= [[0 0] [0 0]] (b/bounds g2)))
      (is (= :a (get g2 [0 0])))))

  (testing "transient dissoc non-boundary"
    (let [grid (sg/map->sparse-grid {[0 0] :a [1 1] :b [2 2] :c})
          t (transient grid)
          _ (dissoc! t [1 1])
          g2 (persistent! t)]
      (is (= 2 (count g2)))
      (is (= [[0 0] [2 2]] (b/bounds g2)))))

  (testing "transient dissoc boundary but not shrinking"
    (let [grid (sg/map->sparse-grid {[0 0] :a [1 0] :b [2 0] :c})
          t (transient grid)
          _ (dissoc! t [1 0])
          g2 (persistent! t)]
      (is (= 2 (count g2)))
      (is (= [[0 0] [2 0]] (b/bounds g2)))))

  (testing "into (uses transient)"
    (let [grid (sg/make-sparse-grid)
          data {[0 0] :a [2 2] :b}
          g2 (into grid data)]
      (is (= 2 (count g2)))
      (is (= [[0 0] [2 2]] (b/bounds g2))))))

(deftest bounded-protocol-test
  (let [grid (sg/map->sparse-grid {[0 0] :a [2 1] :b})]
    (is (= [[0 0] [2 1]] (b/bounds grid)))
    (is (= [0 0] (b/top-left grid)))
    (is (= [2 0] (b/top-right grid)))
    (is (= [2 1] (b/bottom-right grid)))
    (is (= [0 1] (b/bottom-left grid)))
    (is (= [[0 0] [2 0] [2 1] [0 1]] (b/corners grid)))))

(deftest collection-test
  (let [grid (sg/map->sparse-grid {[0 0] :a})]
    (testing "conj"
      (let [g2 (conj grid [[1 1] :b])]
        (is (= :b (get g2 [1 1])))
        (is (= [[0 0] [1 1]] (b/bounds g2)))))
    (testing "seq"
      (is (= #{[[0 0] :a]} (set (seq grid)))))))

(deftest exported-bounded-fns-test
  (let [grid (sg/map->sparse-grid {[0 0] :a [2 1] :b})]
    (testing "exported bounded functions work via sg alias"
      (is (= [[0 0] [2 1]] (sg/bounds grid)))
      (is (= 3 (sg/width grid)))
      (is (= 2 (sg/height grid)))
      (is (= [3 2] (sg/size grid)))
      (is (= [0 0] (sg/top-left grid)))
      (is (= [2 0] (sg/top-right grid)))
      (is (= [2 1] (sg/bottom-right grid)))
      (is (= [0 1] (sg/bottom-left grid)))
      (is (= [[0 0] [2 0] [2 1] [0 1]] (sg/corners grid))))))

(deftest rows->sparse-grid-test
  (testing "basic creation"
    (let [lines ["abc" "def"]
          grid (sg/rows->sparse-grid lines)]
      (is (= 6 (count grid)))
      (is (= \a (get grid [0 0])))
      (is (= \f (get grid [2 1])))
      (is (= [[0 0] [2 1]] (sg/bounds grid)))))

  (testing "with value-fn filtering"
    (let [lines ["a.c" ".e."]
          grid (sg/rows->sparse-grid lines #(when (not= \. %) %))]
      (is (= 3 (count grid)))
      (is (= \a (get grid [0 0])))
      (is (= \c (get grid [2 0])))
      (is (= \e (get grid [1 1])))
      (is (nil? (get grid [1 0])))
      (is (= [[0 0] [2 1]] (sg/bounds grid))))))

(deftest format-rows-test
  (testing "format-rows"
    (let [grid (sg/map->sparse-grid {[0 0] \a [2 1] \b})]
      (is (= ["a.." "..b"] (sg/format-rows grid)))
      (is (= ["a.." "..b"] (sg/format-rows grid :default \.)))
      (is (= ["aXX" "XXb"] (sg/format-rows grid :default \X)))
      (is (= ["A.." "..B"] (sg/format-rows grid :value-fn #(str/upper-case (str %)))))
      (is (= ["a,.,." ".,.,b"] (sg/format-rows grid :col-sep ",")))))
  (testing "format-rows empty"
    (let [grid (sg/make-sparse-grid)]
      (is (= [] (sg/format-rows grid))))))

(deftest format-grid-test
  (testing "format-grid"
    (let [grid (sg/map->sparse-grid {[0 0] \a [2 1] \b})]
      (is (= "a..\n..b" (sg/format-grid grid)))
      (is (= "a..\n..b" (sg/format-grid grid :default \.)))
      (is (= "aXX\nXXb" (sg/format-grid grid :default \X)))
      (is (= "A..\n..B" (sg/format-grid grid :value-fn #(str/upper-case (str %)))))
      (is (= "a,.,.\n.,.,b" (sg/format-grid grid :col-sep ",")))
      (is (= "a..|..b" (sg/format-grid grid :row-sep "|")))))
  (testing "format-grid empty"
    (let [grid (sg/make-sparse-grid)]
      (is (= "" (sg/format-grid grid))))))
