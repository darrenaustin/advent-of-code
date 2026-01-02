(ns aoc.util.pathfinding-test
  (:require
   [aoc.util.pathfinding :as path]
   [clojure.test :refer [deftest is testing]]))

(deftest a-star-cost-test
  (testing "Simple graph with uniform costs"
    (let [graph {:a [:b :c]
                 :b [:d]
                 :c [:d]
                 :d []}
          goal? #(= % :d)]
      (is (= 2 (path/a-star-cost :a graph goal?)))))

  (testing "Graph with weighted edges"
    (let [graph {:a {:b 1 :c 5}
                 :b {:c 2}
                 :c {:d 1}
                 :d {}}
          neighbors (fn [n] (keys (graph n)))
          cost (fn [a b] (get-in graph [a b]))
          goal? #(= % :d)]
      (is (= 4 (path/a-star-cost :a neighbors goal? :cost cost)))))

  (testing "Grid with heuristic"
    (let [neighbors (fn [[x y]]
                      (for [[dx dy] [[0 1] [1 0] [0 -1] [-1 0]]
                            :let [nx (+ x dx) ny (+ y dy)]
                            :when (and (<= 0 nx 2) (<= 0 ny 2))]
                        [nx ny]))
          goal? #(= % [2 2])
          heuristic (fn [[x y]] (+ (Math/abs (- 2 x)) (Math/abs (- 2 y))))]
      (is (= 4 (path/a-star-cost [0 0] neighbors goal? :heuristic heuristic))))))

(deftest a-star-path-test
  (testing "Simple graph with uniform costs"
    (let [graph {:a [:b :c]
                 :b [:d]
                 :c [:d]
                 :d []}
          goal? #(= % :d)
          p (path/a-star-path :a graph goal?)]
      (is (or (= p '(:a :b :d))
              (= p '(:a :c :d))))))

  (testing "Graph with weighted edges"
    (let [graph {:a {:b 1 :c 5}
                 :b {:c 2}
                 :c {:d 1}
                 :d {}}
          neighbors (fn [n] (keys (graph n)))
          cost (fn [a b] (get-in graph [a b]))
          goal? #(= % :d)]
      (is (= '(:a :b :c :d) (path/a-star-path :a neighbors goal? :cost cost)))))

  (testing "Grid with heuristic"
    (let [neighbors (fn [[x y]]
                      (for [[dx dy] [[0 1] [1 0] [0 -1] [-1 0]]
                            :let [nx (+ x dx) ny (+ y dy)]
                            :when (and (<= 0 nx 2) (<= 0 ny 2))]
                        [nx ny]))
          goal? #(= % [2 2])
          heuristic (fn [[x y]] (+ (Math/abs (- 2 x)) (Math/abs (- 2 y))))
          p (path/a-star-path [0 0] neighbors goal? :heuristic heuristic)]
      (is (= 5 (count p)))
      (is (= [0 0] (first p)))
      (is (= [2 2] (last p))))))

(deftest dijkstra-test
  (testing "dijkstra-all-paths"
    (let [graph {:a [:b :c]
                 :b [:d]
                 :c [:d]
                 :d []}
          goal? #(= % :d)
          paths (path/dijkstra-all-paths :a graph goal?)]
      (is (= 2 (count paths)))
      (is (some #(= % '(:a :b :d)) paths))
      (is (some #(= % '(:a :c :d)) paths))))

  (testing "dijkstra-traverse"
    (let [graph {:a [:b :c]
                 :b [:d]
                 :c [:d]
                 :d []}
          m (path/dijkstra-traverse :a graph)]
      (is (= 0 (first (m :a))))
      (is (= 1 (first (m :b))))
      (is (= 1 (first (m :c))))
      (is (= 2 (first (m :d)))))))
