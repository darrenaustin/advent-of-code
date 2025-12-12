(ns aoc.util.bounded-priority-map-test
  (:require
   [aoc.util.bounded-priority-map :refer [bounded-priority-map
                                          bounded-priority-map-by]]
   [clojure.data.priority-map :as pm]
   [clojure.test :refer :all]))

(deftest bounded-priority-map-test
  (testing "basic capacity enforcement"
    (let [bpm (bounded-priority-map 2 :a 1 :b 2)]
      (is (= {:a 1 :b 2} (into {} bpm)))
      (is (= 2 (count bpm)))

      (testing "adding within bounds"
        (let [bpm' (assoc (bounded-priority-map 3 :a 1 :b 2) :c 3)]
          (is (= 3 (count bpm')))
          (is (= {:a 1 :b 2 :c 3} (into {} bpm')))))

      (testing "adding beyond bounds - eviction of worst (highest priority)"
        (let [bpm' (assoc bpm :c 0)]
          (is (= 2 (count bpm')))
          (is (= {:c 0 :a 1} (into {} bpm')))
          (is (not (contains? bpm' :b)))))

      (testing "adding beyond bounds - rejection of worse item"
        (let [bpm' (assoc bpm :c 3)]
          (is (= 2 (count bpm')))
          (is (= {:a 1 :b 2} (into {} bpm')))
          (is (not (contains? bpm' :c)))))))

  (testing "updating existing keys"
    (let [bpm (bounded-priority-map 2 :a 1 :b 2)]
      (testing "update priority"
        (let [bpm' (assoc bpm :a 3)]
          (is (= 2 (count bpm')))
          (is (= {:b 2 :a 3} (into {} bpm')))))

      (testing "update does not trigger eviction logic based on size"
        (let [bpm' (assoc bpm :a 10)]
          (is (= {:a 10 :b 2} (into {} bpm')))))))

  (testing "custom comparator (keeping largest items)"
    (let [bpm (bounded-priority-map-by > 2 :a 10 :b 20)]
      (is (= {:b 20 :a 10} (into {} bpm))) ; sorted by >

      (testing "evicts smallest item (which is at the end)"
        (let [bpm' (assoc bpm :c 30)]
          (is (= {:c 30 :b 20} (into {} bpm')))
          (is (not (contains? bpm' :a)))))

      (testing "rejects small item"
        (let [bpm' (assoc bpm :c 5)]
          (is (= {:b 20 :a 10} (into {} bpm')))
          (is (not (contains? bpm' :c)))))))

  (testing "map operations"
    (let [bpm (bounded-priority-map 2 :a 1 :b 2)]
      (is (= 1 (get bpm :a)))
      (is (= 2 (get bpm :b)))
      (is (nil? (get bpm :c)))
      (is (contains? bpm :a))
      (is (= 1 (count (dissoc bpm :a))))
      (is (empty? (empty bpm)))
      (is (= [:a 1] (first (seq bpm))))))

  (testing "unbounded behavior"
    (let [bpm (bounded-priority-map nil :a 1 :b 2)]
      (is (= 2 (count bpm)))
      (let [bpm' (assoc bpm :c 3 :d 4)]
        (is (= 4 (count bpm')))
        (is (= {:a 1 :b 2 :c 3 :d 4} (into {} bpm'))))))

  (testing "constructor with excess items"
    (let [bpm (bounded-priority-map 2 :a 1 :b 2 :c 3)]
      (is (= 2 (count bpm)))
      (is (= {:a 1 :b 2} (into {} bpm)))
      (is (not (contains? bpm :c)))))

  (testing "equality and hashing"
    (let [bpm1 (bounded-priority-map 2 :a 1 :b 2)
          bpm2 (bounded-priority-map 2 :a 1 :b 2)
          bpm3 (bounded-priority-map 3 :a 1 :b 2)
          bpm4 (bounded-priority-map 2 :a 1 :c 3)]

      (testing "same content and bound"
        (is (= bpm1 bpm2))
        (is (= (hash bpm1) (hash bpm2)))
        (is (= (hash bpm1) (hash bpm2))))

      (testing "same content, different bound"
        (is (not= bpm1 bpm3))
        ;; Hashes might collide, but unlikely for simple cases with different bounds
        (is (not= (hash bpm1) (hash bpm3))))

      (testing "different content, same bound"
        (is (not= bpm1 bpm4)))

      (testing "not equal to standard map or priority map"
        (is (not= bpm1 {:a 1 :b 2}))
        (is (not= bpm1 (pm/priority-map :a 1 :b 2)))))))
