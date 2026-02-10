(ns aoc.util.linked-list-test
  (:require
   [aoc.util.linked-list :as ll]
   [clojure.test :refer [deftest is testing]]))

(defn- to-list [node n]
  (map ll/value (take n (iterate ll/next node))))

(deftest make-circular-node-test
  (testing "creates a single node linked to itself"
    (let [node (ll/make-circular-node 1)]
      (is (= 1 (ll/value node)))
      (is (identical? node (ll/next node)))
      (is (identical? node (ll/previous node))))))

(deftest make-circular-list-test
  (testing "creates a circular list from a collection"
    (let [nodes (ll/make-circular-list [1 2 3])]
      (is (= 3 (count nodes)))
      (let [n1 (nth nodes 0)
            n2 (nth nodes 1)
            n3 (nth nodes 2)]
        (is (= 1 (ll/value n1)))
        (is (= 2 (ll/value n2)))
        (is (= 3 (ll/value n3)))

        (is (identical? n2 (ll/next n1)))
        (is (identical? n3 (ll/next n2)))
        (is (identical? n1 (ll/next n3)))

        (is (identical? n3 (ll/previous n1)))
        (is (identical? n1 (ll/previous n2)))
        (is (identical? n2 (ll/previous n3)))))))

(deftest manipulation-test
  (testing "insert-after inserts a node"
    (let [nodes (ll/make-circular-list [1 2])
          n1 (nth nodes 0)
          n3 (ll/make-circular-node 3)]
      (ll/insert-after! n1 n3)
      (is (= [1 3 2 1] (to-list n1 4)))))

  (testing "inserts node before target"
    (let [nodes (ll/make-circular-list [1 2 3])
          n2 (nth nodes 1)
          new-node (ll/make-circular-node 99)]
      (ll/insert-before! n2 new-node)
      ;; 1 -> 99 -> 2 -> 3
      (is (= [1 99 2 3] (to-list (nth nodes 0) 4)))))

  (testing "remove! removes a node"
    (let [nodes (ll/make-circular-list [1 2 3])
          n2 (nth nodes 1)]
      (ll/remove! n2)
      (is (= [1 3 1] (to-list (nth nodes 0) 3)))
      (is (nil? (ll/next n2)))
      (is (nil? (ll/previous n2))))))

(deftest shift-test
  (testing "shift-right! moves nodes forward"
    (let [nodes (ll/make-circular-list [1 2 3 4 5])
          n1 (nth nodes 0)]
      (ll/shift-right! n1 1)
      (is (= [2 1 3 4 5 2] (to-list (nth nodes 1) 6)))

      (ll/shift-right! n1 2)
      (is (= [2 3 4 1 5 2] (to-list (nth nodes 1) 6)))))

  (testing "shift-left! moves nodes backward"
    (let [nodes (ll/make-circular-list [1 2 3 4 5])
          n1 (nth nodes 0)]
      (ll/shift-left! n1 1)
      ;; 1 moves left of 5. Sequence: 1 -> 5 -> 2 -> 3 -> 4
      (is (= [1 5 2 3 4 1] (to-list n1 6)))

      (ll/shift-left! n1 1)
      ;; 1 moves left of 4. Sequence: 1 -> 4 -> 5 -> 2 -> 3
      (is (= [4 5 2 3 1 4] (to-list (nth nodes 3) 6))))))

(deftest to-string-test
  (testing "toString avoids cycle"
    (let [node (ll/make-circular-node 42)]
      (is (= "42" (str node))))))

(deftest format-test
  (testing "format traverses the list once"
    (let [nodes (ll/make-circular-list [1 2 3])]
      (is (= "(1 2 3)" (ll/format (nth nodes 0))))
      (is (= "(2 3 1)" (ll/format (nth nodes 1)))))))
