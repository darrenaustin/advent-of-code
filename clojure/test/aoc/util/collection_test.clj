(ns aoc.util.collection-test
  (:require [aoc.util.collection :as c]
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
    (is (zero? (c/count-where even? [1 3 5 7])))
    (is (zero? (c/count-where pos? [])))
    (is (= 5 (c/count-where pos? [1 2 3 4 5])))
    (is (= 2 (c/count-where #(> % 10) [5 15 8 20 3])))))

(deftest split-test
  (testing "split divides a collection based on a separator predicate"
    (is (= [[1 2] [3 4]] (c/split [1 2 0 3 4] zero?)))
    (is (= [[1 2] [3 4]] (c/split [1 2 0 0 3 4] zero?)))
    (is (= [[2 0 0] [4]] (c/split [1 2 0 0 3 4] odd?)))
    (is (= [[1 2] [3 4]] (c/split [0 1 2 0 3 4 0] zero?)))
    (is (= [[1 2 3]] (c/split [1 2 3] zero?)))
    (is (nil? (c/split [] zero?)))
    (is (nil? (c/split [0 0 0] zero?)))
    (is (= [[1] [2] [3]] (c/split [1 0 2 0 3] zero?))))

  (testing "split returns a lazy sequence"
    (let [infinite-ints (iterate inc 1) ; 1 2 3 ...
          split-lazy (c/split infinite-ints #(zero? (mod % 3)))] ; split on multiples of 3
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
           (c/val->key {:a 1 :b 2})))
    (is (= {} (c/val->key {})))
    (is (= {"foo" :x, "bar" :y}
           (c/val->key {:x "foo" :y "bar"})))
    ; For duplicate values, one key will win (implementation dependent)
    (let [result (c/val->key {:a 1 :b 1})]
      (is (= 1 (count result)))
      (is (contains? #{:a :b} (get result 1))))))

(deftest indexed-test
  (testing "indexed creates index-value pairs"
    (is (= [[0 :a] [1 :b] [2 :c]]
           (c/indexed [:a :b :c])))
    (is (= [] (c/indexed [])))
    (is (= [[0 "first"] [1 "second"]]
           (c/indexed ["first" "second"])))
    (is (= [[0 1] [1 4] [2 9]]
           (c/indexed [1 4 9])))))

(deftest iterate-n-test
  (testing "iterate-n applies function n times"
    (is (= 8 (c/iterate-n inc 5 3)))
    (is (= 5 (c/iterate-n inc 5 0)))
    (is (= 16 (c/iterate-n #(* % 2) 2 3)))
    (is (= [1 1 2] (c/iterate-n #(conj % (count %)) [1] 2)))
    (is (= "hello" (c/iterate-n identity "hello" 5)))))

(deftest iteration-with-cycle-test
  (testing "iteration-with-cycle detects cycles and jumps ahead"
    ; Simple cycle: 0 -> 1 -> 2 -> 0 -> 1 -> 2 ...
    (let [cycle-fn #(mod (inc %) 3)]
      (is (= 1 (c/iteration-with-cycle 1 cycle-fn 0)))
      (is (= 2 (c/iteration-with-cycle 2 cycle-fn 0)))
      (is (zero? (c/iteration-with-cycle 3 cycle-fn 0)))
      (is (= 1 (c/iteration-with-cycle 4 cycle-fn 0)))
      (is (= 1 (c/iteration-with-cycle 1000000 cycle-fn 0))))

    ; Fixed point
    (is (= 5 (c/iteration-with-cycle 100 identity 5)))))

(deftest transpose-test
  (testing "transpose swaps rows and columns"
    (is (= [[1 3] [2 4]]
           (c/transpose [[1 2] [3 4]])))
    (is (= [[1 4 7] [2 5 8] [3 6 9]]
           (c/transpose [[1 2 3] [4 5 6] [7 8 9]])))
    (is (= [[1] [2] [3]]
           (c/transpose [[1 2 3]])))))

(deftest flip-horizontal-test
  (testing "flip-horizontal reverses each row"
    (is (= [[2 1] [4 3]]
           (c/flip-horizontal [[1 2] [3 4]])))
    (is (= [[3 2 1] [6 5 4]]
           (c/flip-horizontal [[1 2 3] [4 5 6]])))
    (is (= [] (c/flip-horizontal [])))
    (is (= [[1]] (c/flip-horizontal [[1]])))))

(deftest rotate-right-test
  (testing "rotate-right rotates 90 degrees clockwise"
    (is (= [[3 1] [4 2]]
           (c/rotate-right [[1 2] [3 4]])))
    (is (= [[7 4 1] [8 5 2] [9 6 3]]
           (c/rotate-right [[1 2 3] [4 5 6] [7 8 9]])))
    (is (= [[1]] (c/rotate-right [[1]])))))

(deftest asc-desc-test
  (testing "asc and desc comparison functions"
    (is (neg? (c/asc 1 2)))
    (is (zero? (c/asc 5 5)))
    (is (pos? (c/asc 3 1)))

    (is (pos? (c/desc 1 2)))
    (is (zero? (c/desc 5 5)))
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

