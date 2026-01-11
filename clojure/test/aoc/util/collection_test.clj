(ns aoc.util.collection-test
  (:require
   [aoc.util.collection :as c]
   [clojure.test :refer :all]))

(deftest first-where-test
  (testing "first-where finds the first element that satisfies the predicate"
    (is (= 4 (c/first-where even? [1 3 4 6 8])))
    (is (= :b (c/first-where keyword? [1 2 :b :c 5])))
    (is (nil? (c/first-where even? [1 3 5 7])))
    (is (nil? (c/first-where pos? [])))
    (is (= 1 (c/first-where pos? [1 2 3])))))

(deftest count-where-test
  (testing "count-where counts elements that satisfy the predicate"
    (is (= 3 (c/count-where even? [1 2 3 4 5 6])))
    (is (= 0 (c/count-where even? [1 3 5 7])))
    (is (= 0 (c/count-where pos? [])))
    (is (= 5 (c/count-where pos? [1 2 3 4 5])))
    (is (= 2 (c/count-where #(> % 10) [5 15 8 20 3])))))

(deftest split-test
  (testing "split divides a collection based on a separator predicate"
    (is (= [[1 2] [3 4]] (c/split zero? [1 2 0 3 4])))
    (is (= [[1 2] [3 4]] (c/split zero? [1 2 0 0 3 4])))
    (is (= [[2 0 0] [4]] (c/split odd? [1 2 0 0 3 4])))
    (is (= [[1 2] [3 4]] (c/split zero? [0 1 2 0 3 4 0])))
    (is (= [[1 2 3]] (c/split zero? [1 2 3])))
    (is (nil? (c/split zero? [])))
    (is (nil? (c/split zero? [0 0 0])))
    (is (= [[1] [2] [3]] (c/split zero? [1 0 2 0 3]))))

  (testing "split returns a lazy sequence"
    (let [infinite-ints (iterate inc 1) ; 1 2 3 ...
          split-lazy (c/split #(zero? (mod % 3)) infinite-ints)] ; split on multiples of 3
      (is (= [[1 2] [4 5] [7 8]] (take 3 split-lazy))))))

(deftest group-by-value-test
  (testing "group-by-value groups keys by their values"
    (letfn [(sorted-result [m] (update-vals (c/group-by-value m) sort))]
      (is (= {1 [:a :c] 2 [:b]} (sorted-result {:a 1 :b 2 :c 1})))
      (is (= {} (sorted-result {})))
      (is (= {10 [:x :y :z]} (sorted-result {:x 10 :y 10 :z 10})))
      (is (= {"foo" [:a] "bar" [:b] "baz" [:c]}
             (sorted-result {:a "foo" :b "bar" :c "baz"}))))))

(deftest val->key-test
  (testing "val->key inverts a map"
    (is (= {1 :a, 2 :b}
           (c/vals->keys {:a 1 :b 2})))
    (is (= {} (c/vals->keys {})))
    (is (= {"foo" :x, "bar" :y}
           (c/vals->keys {:x "foo" :y "bar"})))
    ; For duplicate values, one key will win (implementation dependent)
    (let [result (c/vals->keys {:a 1 :b 1})]
      (is (= 1 (count result)))
      (is (contains? #{:a :b} (get result 1))))))

(deftest keys-when-val-test
  (testing "keys-when-val returns keys where value matches predicate"
    (is (= [:a :c] (sort (c/keys-when-val odd? {:a 1 :b 2 :c 3}))))
    (is (empty? (c/keys-when-val neg? {:a 1 :b 2})))
    (is (= [:b] (c/keys-when-val #{2} {:a 1 :b 2})))))

(deftest map-by-test
  (testing "map-by creates a map keyed by function result"
    (is (= {1 {:id 1 :val "a"} 2 {:id 2 :val "b"}}
           (c/map-by :id [{:id 1 :val "a"} {:id 2 :val "b"}])))
    (is (= {1 "a" 3 "abc"} (c/map-by count ["a" "abc"])))))

(deftest map-accum-test
  (testing "map-accum accumulates state and returns mapped sequence"
    (let [f (fn [acc x] [(+ acc x) (+ acc x)])]
      (is (= [6 [1 3 6]] (c/map-accum f 0 [1 2 3])))))
  (testing "map-accum threads state correctly"
    (let [f (fn [sum x]
              (let [new-sum (+ sum x)]
                [new-sum (* new-sum 2)]))]
      (is (= [6 [2 6 12]] (c/map-accum f 0 [1 2 3]))))))

(deftest dissoc-in-test
  (testing "dissoc-in removes nested keys"
    (is (= {:a {:b 2}} (c/dissoc-in {:a {:b 2 :c 3}} [:a :c])))
    (is (= {:a {}} (c/dissoc-in {:a {:b 2}} [:a :b])))
    (is (= {} (c/dissoc-in {:a 1} [:a])))
    (is (= {:a 1} (c/dissoc-in {:a 1} [:b])))
    (is (= {:a {:b 1}} (c/dissoc-in {:a {:b 1}} [:a :c])))))

(deftest indexed-test
  (testing "indexed creates index-value pairs"
    (is (= [[0 :a] [1 :b] [2 :c]]
           (c/indexed [:a :b :c])))
    (is (= [] (c/indexed [])))
    (is (= [[0 "first"] [1 "second"]]
           (c/indexed ["first" "second"])))
    (is (= [[0 1] [1 4] [2 9]]
           (c/indexed [1 4 9])))))

(deftest index-test
  (testing "index returns the index of the first occurrence of a value"
    (is (= 1 (c/index [:a :b :c] :b)))
    (is (= 0 (c/index [:a :b :c] :a)))
    (is (= 2 (c/index [:a :b :c] :c)))
    (is (nil? (c/index [:a :b :c] :d)))
    (is (nil? (c/index [] :a)))
    (is (= 1 (c/index [1 2 3 2 1] 2)))))

(deftest indexes-by-test
  (testing "indexes-by returns indices matching predicate"
    (is (= [0 2] (c/indexes-by odd? [1 2 3 4])))
    (is (empty? (c/indexes-by neg? [1 2 3])))))

(deftest first-duplicate-test
  (testing "first-duplicate returns the first element that appears more than once"
    (is (= 2 (c/first-duplicate [1 2 3 2 4])))
    (is (= :a (c/first-duplicate [:a :b :c :a])))
    (is (nil? (c/first-duplicate [1 2 3 4])))
    (is (nil? (c/first-duplicate [])))
    (is (= 1 (c/first-duplicate [1 1])))
    (is (= 3 (c/first-duplicate [1 2 3 4 3 2 1])))))

(deftest rotate-left-test
  (testing "rotate-left rotates the elements of the collection by n to the left, wrapping"
    (is (= [2 3 4 5 0 1] (c/rotate-left 2 [0 1 2 3 4 5])))
    (is (= [0 1 2 3 4 5] (c/rotate-left 0 [0 1 2 3 4 5])))
    (is (= [4 5 0 1 2 3] (c/rotate-left 10 [0 1 2 3 4 5])))
    (is (= [1] (c/rotate-left 3 [1])))))

(deftest rotate-right-test
  (testing "rotate-right rotates the elements of the collection by n to the right, wrapping"
    (is (= [4 5 0 1 2 3] (c/rotate-right 2 [0 1 2 3 4 5])))
    (is (= [0 1 2 3 4 5] (c/rotate-right 0 [0 1 2 3 4 5])))
    (is (= [2 3 4 5 0 1] (c/rotate-right 10 [0 1 2 3 4 5])))
    (is (= [1] (c/rotate-right 3 [1])))))

(deftest pairs-test
  (testing "pairs returns all unique combinations of size 2"
    (is (= [[1 2] [1 3] [2 3]] (c/pairs [1 2 3])))
    (is (= [[:a :b] [:a :c] [:a :d] [:b :c] [:b :d] [:c :d]] (c/pairs [:a :b :c :d])))
    (is (empty? (c/pairs [1])))
    (is (empty? (c/pairs [])))
    (is (= [[1 1] [1 2] [1 2]] (c/pairs [1 1 2])) "Duplicates in input are treated as distinct elements")))

(deftest adjacent-pairs-test
  (testing "adjacent-pairs returns sliding window of pairs"
    (is (= [[1 2] [2 3]] (c/adjacent-pairs [1 2 3])))
    (is (= [[:a :b] [:b :c] [:c :d]] (c/adjacent-pairs [:a :b :c :d])))
    (is (empty? (c/adjacent-pairs [1])))
    (is (empty? (c/adjacent-pairs [])))))

(deftest cyclic-adjacent-pairs-test
  (testing "cyclic-adjacent-pairs returns sliding window of pairs with wrap-around"
    (is (= [[1 2] [2 3] [3 1]] (c/cyclic-adjacent-pairs [1 2 3])))
    (is (= [[:a :b] [:b :c] [:c :d] [:d :a]] (c/cyclic-adjacent-pairs [:a :b :c :d])))
    (is (= [[1 1]] (c/cyclic-adjacent-pairs [1])))
    (is (empty? (c/cyclic-adjacent-pairs [])))))

(deftest nth-iteration-test
  (testing "nth-iteration applies function n times"
    (is (= 8 (c/nth-iteration inc 5 3)))
    (is (= 5 (c/nth-iteration inc 5 0)))
    (is (= 16 (c/nth-iteration #(* % 2) 2 3)))
    (is (= [1 1 2] (c/nth-iteration #(conj % (count %)) [1] 2)))
    (is (= "hello" (c/nth-iteration identity "hello" 5)))))

(deftest iteration-with-cycle-test
  (testing "iteration-with-cycle detects cycles and jumps ahead"
    ; Simple cycle: 0 -> 1 -> 2 -> 0 -> 1 -> 2 ...
    (let [cycle-fn #(mod (inc %) 3)]
      (is (= 1 (c/iteration-with-cycle 1 cycle-fn 0)))
      (is (= 2 (c/iteration-with-cycle 2 cycle-fn 0)))
      (is (= 0 (c/iteration-with-cycle 3 cycle-fn 0)))
      (is (= 1 (c/iteration-with-cycle 4 cycle-fn 0)))
      (is (= 1 (c/iteration-with-cycle 1000000 cycle-fn 0))))

    ; Fixed point
    (is (= 5 (c/iteration-with-cycle 100 identity 5)))))

(deftest asc-desc-test
  (testing "asc and desc comparison functions"
    (is (neg? (c/asc 1 2)))
    (is (= 0 (c/asc 5 5)))
    (is (pos? (c/asc 3 1)))

    (is (pos? (c/desc 1 2)))
    (is (= 0 (c/desc 5 5)))
    (is (neg? (c/desc 3 1)))))

(deftest by-test
  (testing "by creates multi-key comparator"
    (let [people [{:name "Alice" :age 30 :salary 50000}
                  {:name "Bob" :age 25 :salary 60000}
                  {:name "Charlie" :age 30 :salary 45000}]]

      ; Sort by age ascending
      (let [sorted-by-age (->> people
                               (sort (c/by :age c/asc))
                               (map :name))]
        (is (= "Bob" (first sorted-by-age)))  ; Bob is youngest
        (is (contains? #{"Alice" "Charlie"} (second sorted-by-age))))

      ; Sort by age ascending, then salary descending
      (let [sorted-multi (->> people
                              (sort (c/by :age c/asc :salary c/desc))
                              (map :name))]
        (is (= "Bob" (first sorted-multi)))  ; Bob is youngest
        (is (= "Alice" (second sorted-multi)))  ; Among 30-year-olds, Alice has higher salary
        (is (= "Charlie" (last sorted-multi))))  ; Charlie has lower salary

      ; Sort by salary descending
      (let [sorted-by-salary (->> people
                                  (sort (c/by :salary c/desc))
                                  (map :name))]
        (is (= "Bob" (first sorted-by-salary)))  ; Bob has highest salary
        (is (= "Charlie" (last sorted-by-salary)))))))

(deftest pad-left-test
  (testing "pad-left adds padding to reach target length"
    (is (= [0 0 1 2 3] (vec (c/pad-left [1 2 3] 5 0))))
    (is (= [1 2 3] (c/pad-left [1 2 3] 3 0)))
    (is (= [1 2 3] (c/pad-left [1 2 3] 2 0)))
    (is (= [:x :x :x :a :b] (vec (c/pad-left [:a :b] 5 :x))))
    (is (= [] (vec (c/pad-left [] 0 nil))))))

