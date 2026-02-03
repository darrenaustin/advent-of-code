(ns aoc.util.grid-test
  (:require
   [aoc.util.grid :as g]
   [clojure.string :as str]
   [clojure.test :refer [deftest is testing]]))

;; --- Dense Grid Tests ---

(deftest dense-grid-creation-test
  (testing "make-grid"
    (let [grid (g/make-grid 3 2 :init)]
      (is (= 3 (g/width grid)))
      (is (= 2 (g/height grid)))
      (is (= [3 2] (g/size grid)))
      (is (= :init (get grid [0 0])))
      (is (= :init (get grid [2 1])))))

  (testing "rows->grid with strings"
    (let [grid (g/rows->grid ["abc" "def"])]
      (is (= 3 (g/width grid)))
      (is (= 2 (g/height grid)))
      (is (= \a (get grid [0 0])))
      (is (= \f (get grid [2 1])))))

  (testing "rows->grid with vectors"
    (let [grid (g/rows->grid [[1 2] [3 4]])]
      (is (= 2 (g/width grid)))
      (is (= 2 (g/height grid)))
      (is (= 1 (get grid [0 0])))
      (is (= 4 (get grid [1 1])))))

  (testing "rows->grid with value-fn"
    (let [grid (g/rows->grid ["12" "34"] #(Long/parseLong (str %)))]
      (is (= 1 (get grid [0 0])))
      (is (= 4 (get grid [1 1]))))))

(deftest dense-associative-test
  (let [grid (g/rows->grid ["abc" "def"])]
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

(deftest dense-counted-test
  (let [grid (g/rows->grid ["abc" "def"])]
    (is (= 6 (count grid)))))

(deftest dense-iterable-test
  (let [grid (g/rows->grid [[1 2] [3 4]])]
    (is (instance? Iterable grid))
    ;; Check basic iteration via into (uses iterator)
    (is (= [[[0 0] 1] [[1 0] 2] [[0 1] 3] [[1 1] 4]]
           #_{:splint/disable [style lint/into-literal]}
           (into [] grid)))
    ;; Check transducer usage
    (is (= 10 (transduce (map val) + 0 grid)))))

(deftest dense-seqable-test
  (let [grid (g/rows->grid ["ab" "cd"])]
    (is (= [[[0 0] \a] [[1 0] \b] [[0 1] \c] [[1 1] \d]]
           (seq grid)))))

(deftest dense-ifn-test
  (let [grid (g/rows->grid ["abc"])]
    (is (= \a (grid [0 0])))
    (is (= :default (grid [5 5] :default)))))

(deftest dense-equality-test
  (let [g1 (g/rows->grid ["abc"])
        g2 (g/rows->grid ["abc"])
        g3 (g/rows->grid ["abd"])]
    (is (= g1 g2))
    (is (not= g1 g3))
    (is (= (hash g1) (hash g2)))))

(deftest dense-update-grid-test
  (let [grid (g/rows->grid ["12" "34"] #(Long/parseLong (str %)))
        g2 (g/update-grid grid (fn [[_ v]] (inc v)))]
    (is (= 2 (get g2 [0 0])))
    (is (= 5 (get g2 [1 1])))))

(deftest dense-collection-test
  (let [grid (g/rows->grid ["ab" "cd"])]
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

(deftest dense-metadata-test
  (let [grid (g/make-grid 2 2)
        m {:a 1}
        grid-with-meta (with-meta grid m)]
    (is (= m (meta grid-with-meta)))
    (is (= grid grid-with-meta) "Metadata should not affect equality")
    (is (= m (meta (assoc grid-with-meta [0 0] :val))) "Metadata should be preserved after assoc")))

(deftest dense-transient-test
  (let [grid (g/make-grid 2 2 0)]
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

(deftest dense-transformation-test
  (let [grid (g/rows->grid ["ab" "cd"])]
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

(deftest dense-sub-grid-test
  (let [grid (g/rows->grid ["abc" "def" "ghi"])]
    (testing "sub-grid extraction"
      (let [sub (g/sub-grid grid [1 1] [2 2])]
        (is (= 2 (g/width sub)))
        (is (= 2 (g/height sub)))
        (is (= \e (get sub [0 0])))
        (is (= \f (get sub [1 0])))
        (is (= \h (get sub [0 1])))
        (is (= \i (get sub [1 1])))))

    (testing "set-sub-grid"
      (let [sub (g/rows->grid ["XY" "Z!"])
            updated (g/set-sub-grid grid [1 1] sub)]
        (is (= \a (get updated [0 0])))
        (is (= \X (get updated [1 1])))
        (is (= \Y (get updated [2 1])))
        (is (= \Z (get updated [1 2])))
        (is (= \! (get updated [2 2])))))

    (testing "set-sub-grid clipping"
      (let [sub (g/rows->grid ["XY" "Z!"])
            updated (g/set-sub-grid grid [2 2] sub)]
        (is (= \X (get updated [2 2])))
        (is (= 3 (g/width updated)))
        (is (= 3 (g/height updated)))))))

(deftest dense-row-col-access-test
  (let [grid (g/rows->grid ["abc" "def" "ghi"])]
    (testing "column access"
      (is (= [\a \d \g] (g/column grid 0)))
      (is (= [\b \e \h] (g/column grid 1)))
      (is (= [\c \f \i] (g/column grid 2))))

    (testing "set-column"
      (let [g2 (g/set-column grid 0 [\1 \2])]
        (is (= \1 (get g2 [0 0])))
        (is (= \2 (get g2 [0 1])))
        (is (= \g (get g2 [0 2])))
        (is (= \b (get g2 [1 0]))))
      (let [g3 (g/set-column grid 1 [\X])]
        (is (= \X (get g3 [1 0])))
        (is (= \e (get g3 [1 1]))))
      (let [g4 (g/set-column grid 0 [\1 \2 \3 \4])]
        (is (= \1 (get g4 [0 0])))
        (is (= \2 (get g4 [0 1])))
        (is (= \3 (get g4 [0 2])))))

    (testing "row access"
      (is (= [\a \b \c] (g/row grid 0)))
      (is (= [\d \e \f] (g/row grid 1)))
      (is (= [\g \h \i] (g/row grid 2))))

    (testing "set-row"
      (let [g2 (g/set-row grid 0 [\1 \2 \3])]
        (is (= "123" (first (str/split-lines (g/format g2)))))
        (is (= "def" (second (str/split-lines (g/format g2))))))
      (let [g3 (g/set-row grid 1 [\X])]
        (is (= "Xef" (second (str/split-lines (g/format g3))))))
      (let [g4 (g/set-row grid 0 [\1 \2 \3 \4])]
        (is (= "123" (first (str/split-lines (g/format g4)))))))

    (testing "convenience accessors"
      (is (= [\a \b \c] (g/top-row grid)))
      (is (= [\g \h \i] (g/bottom-row grid)))
      (is (= [\a \d \g] (g/left-column grid)))
      (is (= [\c \f \i] (g/right-column grid))))))

;; --- Sparse Grid Tests ---

(deftest sparse-grid-creation-test
  (testing "make-sparse-grid"
    (let [grid (g/->sparse-grid)]
      (is (= 0 (count grid)))
      (is (nil? (g/bounds grid)))
      (is (= 0 (g/width grid)))
      (is (= 0 (g/height grid)))))

  (testing "map->sparse-grid"
    (let [m {[0 0] :a [2 2] :b}
          grid (g/map->sparse-grid m)]
      (is (= 2 (count grid)))
      (is (= [[0 0] [2 2]] (g/bounds grid)))
      (is (= 3 (g/width grid)))
      (is (= 3 (g/height grid)))
      (is (= :a (get grid [0 0])))
      (is (= :b (get grid [2 2]))))))

(deftest sparse-associative-test
  (let [grid (g/->sparse-grid)]
    (testing "assoc expands bounds"
      (let [g1 (assoc grid [0 0] :a)]
        (is (= [[0 0] [0 0]] (g/bounds g1)))
        (let [g2 (assoc g1 [2 1] :b)]
          (is (= [[0 0] [2 1]] (g/bounds g2)))
          (is (= 3 (g/width g2)))
          (is (= 2 (g/height g2))))))

    (testing "dissoc shrinks bounds"
      (let [grid (g/map->sparse-grid {[0 0] :a [2 2] :b})
            g2 (dissoc grid [2 2])]
        (is (= [[0 0] [0 0]] (g/bounds g2)))
        (is (= 1 (g/width g2)))
        (is (= 1 (g/height g2))))
      (let [grid (g/map->sparse-grid {[0 0] :a [2 2] :b})
            g2 (dissoc grid [1 1])] ;; Removing non-existent key
        (is (= [[0 0] [2 2]] (g/bounds g2)))))

    (testing "dissoc edge point (non-corner)"
      (let [grid (g/map->sparse-grid {[0 0] :a [1 0] :b [2 0] :c [1 1] :d})]
        ;; Bounds are [[0 0] [2 1]]
        (is (= [[0 0] [2 1]] (g/bounds grid)))

        ;; Remove [1 1]. It is at y=1 (max-y). It is the ONLY point at max-y.
        ;; Should trigger recalc and shrink bounds to [[0 0] [2 0]].
        (let [g2 (dissoc grid [1 1])]
          (is (= [[0 0] [2 0]] (g/bounds g2))))

        ;; Remove [1 0]. It is at y=0 (min-y). But [0 0] and [2 0] are also at min-y.
        ;; Should trigger recalc, but bounds remain [[0 0] [2 1]].
        (let [g3 (dissoc grid [1 0])]
          (is (= [[0 0] [2 1]] (g/bounds g3))))))

    (testing "dissoc to empty"
      (let [grid (g/map->sparse-grid {[0 0] :a})
            g2 (dissoc grid [0 0])]
        (is (nil? (g/bounds g2)))
        (is (= 0 (count g2)))))))

(deftest sparse-transient-test
  (testing "transient assoc"
    (let [grid (g/->sparse-grid)
          t (transient grid)
          _ (assoc! t [0 0] :a)
          _ (assoc! t [2 2] :b)
          g2 (persistent! t)]
      (is (= 2 (count g2)))
      (is (= [[0 0] [2 2]] (g/bounds g2)))
      (is (= :a (get g2 [0 0])))
      (is (= :b (get g2 [2 2])))))

  (testing "transient dissoc"
    (let [grid (g/map->sparse-grid {[0 0] :a [2 2] :b})
          t (transient grid)
          _ (dissoc! t [2 2])
          g2 (persistent! t)]
      (is (= 1 (count g2)))
      (is (= [[0 0] [0 0]] (g/bounds g2)))
      (is (= :a (get g2 [0 0])))))

  (testing "transient dissoc non-boundary"
    (let [grid (g/map->sparse-grid {[0 0] :a [1 1] :b [2 2] :c})
          t (transient grid)
          _ (dissoc! t [1 1])
          g2 (persistent! t)]
      (is (= 2 (count g2)))
      (is (= [[0 0] [2 2]] (g/bounds g2)))))

  (testing "transient dissoc boundary but not shrinking"
    (let [grid (g/map->sparse-grid {[0 0] :a [1 0] :b [2 0] :c})
          t (transient grid)
          _ (dissoc! t [1 0])
          g2 (persistent! t)]
      (is (= 2 (count g2)))
      (is (= [[0 0] [2 0]] (g/bounds g2)))))

  (testing "into (uses transient)"
    (let [grid (g/->sparse-grid)
          data {[0 0] :a [2 2] :b}
          g2 (into grid data)]
      (is (= 2 (count g2)))
      (is (= [[0 0] [2 2]] (g/bounds g2))))))

(deftest sparse-bounded-protocol-test
  (let [grid (g/map->sparse-grid {[0 0] :a [2 1] :b})]
    (is (= [[0 0] [2 1]] (g/bounds grid)))
    (is (= [0 0] (g/top-left grid)))
    (is (= [2 0] (g/top-right grid)))
    (is (= [2 1] (g/bottom-right grid)))
    (is (= [0 1] (g/bottom-left grid)))
    (is (= [[0 0] [2 0] [2 1] [0 1]] (g/corners grid)))))

(deftest sparse-collection-test
  (let [grid (g/map->sparse-grid {[0 0] :a})]
    (testing "conj"
      (let [g2 (conj grid [[1 1] :b])]
        (is (= :b (get g2 [1 1])))
        (is (= [[0 0] [1 1]] (g/bounds g2)))))
    (testing "seq"
      (is (= #{[[0 0] :a]} (set (seq grid)))))))

(deftest sparse-rows->sparse-grid-test
  (testing "basic creation"
    (let [lines ["abc" "def"]
          grid (g/rows->sparse-grid lines)]
      (is (= 6 (count grid)))
      (is (= \a (get grid [0 0])))
      (is (= \f (get grid [2 1])))
      (is (= [[0 0] [2 1]] (g/bounds grid)))))

  (testing "with value-fn filtering"
    (let [lines ["a.c" ".e."]
          grid (g/rows->sparse-grid lines #(when (not= \. %) %))]
      (is (= 3 (count grid)))
      (is (= \a (get grid [0 0])))
      (is (= \c (get grid [2 0])))
      (is (= \e (get grid [1 1])))
      (is (nil? (get grid [1 0])))
      (is (= [[0 0] [2 1]] (g/bounds grid))))))

(deftest sparse-format-test
  (testing "format"
    (let [grid (g/map->sparse-grid {[0 0] \a [2 1] \b})]
      (is (= "a..\n..b" (g/format grid)))
      (is (= "a..\n..b" (g/format grid :default \.)))
      (is (= "aXX\nXXb" (g/format grid :default \X)))
      (is (= "A..\n..B" (g/format grid :value-fn #(str/upper-case (str %)))))
      (is (= "a,.,.\n.,.,b" (g/format grid :col-sep ",")))
      (is (= "a..|..b" (g/format grid :row-sep "|")))))
  (testing "format empty"
    (let [grid (g/->sparse-grid)]
      (is (= "" (g/format grid))))))

(deftest bounded-helpers-test
  (testing "edge and corner helpers"
    ;; Grid bounds: x: 10..20, y: 5..15
    (let [grid (g/map->sparse-grid {[10 5] :tl [20 5] :tr [10 15] :bl [20 15] :br})]
      (is (= 5  (g/top grid)))
      (is (= 15 (g/bottom grid)))
      (is (= 10 (g/left grid)))
      (is (= 20 (g/right grid)))

      (is (= [10 5]  (g/top-left grid)))
      (is (= [20 5]  (g/top-right grid)))
      (is (= [10 15] (g/bottom-left grid)))
      (is (= [20 15] (g/bottom-right grid)))

      (is (= [[10 5] [20 5] [20 15] [10 15]] (g/corners grid))))))

(deftest ->sparse-grid-test
  (testing "empty"
    (is (= (g/make-sparse-grid) (g/->sparse-grid))))

  (testing "from map"
    (let [m {[0 0] :a [1 1] :b}
          grid (g/->sparse-grid m)]
      (is (= :a (get grid [0 0])))
      (is (= :b (get grid [1 1])))))

  (testing "from string"
    (let [s "ab\ncd"
          grid (g/->sparse-grid s)]
      (is (= \a (get grid [0 0])))
      (is (= \d (get grid [1 1]))))
    (let [s "12\n34"
          grid (g/->sparse-grid s #(Long/parseLong (str %)))]
      (is (= 1 (get grid [0 0])))
      (is (= 4 (get grid [1 1])))))

  (testing "from rows (vector)"
    (let [rows ["ab" "cd"]
          grid (g/->sparse-grid rows)]
      (is (= \a (get grid [0 0])))
      (is (= \d (get grid [1 1]))))
    (let [rows ["12" "34"]
          grid (g/->sparse-grid rows #(Long/parseLong (str %)))]
      (is (= 1 (get grid [0 0])))
      (is (= 4 (get grid [1 1]))))))

(deftest ->grid-test
  (testing "from string"
    (let [s "ab\ncd"
          grid (g/->grid s)]
      (is (= 2 (g/width grid)))
      (is (= 2 (g/height grid)))
      (is (= \a (get grid [0 0])))))

  (testing "from rows"
    (let [rows ["ab" "cd"]
          grid (g/->grid rows)]
      (is (= 2 (g/width grid)))
      (is (= 2 (g/height grid)))
      (is (= \a (get grid [0 0])))))

  (testing "make-grid 2-arity"
    (let [grid (g/->grid 2 3)]
      (is (= 2 (g/width grid)))
      (is (= 3 (g/height grid)))
      (is (nil? (get grid [0 0])))))

  (testing "make-grid 3-arity"
    (let [grid (g/->grid 2 3 :x)]
      (is (= 2 (g/width grid)))
      (is (= 3 (g/height grid)))
      (is (= :x (get grid [0 0])))))

  (testing "with value-fn (string)"
    (let [grid (g/->grid "12\n34" #(Long/parseLong (str %)))]
      (is (= 1 (get grid [0 0])))))

  (testing "with value-fn (rows)"
    (let [grid (g/->grid ["12" "34"] #(Long/parseLong (str %)))]
      (is (= 1 (get grid [0 0])))))

  (testing "invalid args"
    (is (thrown? IllegalArgumentException (g/->grid :invalid)))
    (is (thrown? IllegalArgumentException (g/->grid :invalid :args)))
    (is (thrown? IllegalArgumentException (g/->grid 1 "2" :x)))))

(deftest map->sparse-grid-value-fn-test
  (testing "map->sparse-grid with value-fn"
    (let [m {[0 0] "1" [1 1] "2"}
          grid (g/map->sparse-grid m #(Long/parseLong %))]
      (is (= 1 (get grid [0 0])))
      (is (= 2 (get grid [1 1])))))

  (testing "map->sparse-grid with value-fn removing nils"
    (let [m {[0 0] 1 [1 1] 2}
          grid (g/map->sparse-grid m #(when (even? %) %))]
      (is (nil? (get grid [0 0])))
      (is (= 2 (get grid [1 1])))
      (is (= 1 (count grid))))))

(deftest ->sparse-grid-map-value-fn-test
  (testing "->sparse-grid with map and value-fn"
    (let [m {[0 0] "1" [1 1] "2"}
          grid (g/->sparse-grid m #(Long/parseLong %))]
      (is (= 1 (get grid [0 0])))
      (is (= 2 (get grid [1 1]))))))

(deftest dense-format-test
  (testing "format"
    (let [grid (g/rows->grid ["abc" "def"])]
      (is (= "abc\ndef" (g/format grid)))
      (is (= "ABC\nDEF" (g/format grid :value-fn #(str/upper-case (str %)))))
      (is (= "a,b,c\nd,e,f" (g/format grid :col-sep ",")))
      (is (= "abc|def" (g/format grid :row-sep "|")))))
  (testing "format empty"
    (let [grid (g/make-grid 0 0)]
      (is (= "" (g/format grid))))))

(deftest format-rows-test
  (testing "format-rows dense"
    (let [grid (g/rows->grid ["abc" "def"])]
      (is (= ["abc" "def"] (g/format-rows grid)))
      (is (= ["ABC" "DEF"] (g/format-rows grid :value-fn #(str/upper-case (str %)))))))

  (testing "format-rows sparse"
    (let [grid (g/map->sparse-grid {[0 0] \a [2 1] \b})]
      (is (= ["a.." "..b"] (g/format-rows grid)))
      (is (= ["aXX" "XXb"] (g/format-rows grid :default \X)))))

  (testing "format-rows empty"
    (is (= [] (g/format-rows (g/make-grid 0 0))))
    (is (= [] (g/format-rows (g/make-sparse-grid))))))
