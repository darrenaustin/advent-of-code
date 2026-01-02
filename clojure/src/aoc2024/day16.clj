;; https://adventofcode.com/2024/day/16
(ns aoc2024.day16
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.grid :as g]
   [aoc.util.math :as m]
   [aoc.util.pathfinding :as path]
   [aoc.util.pos :as p]))

(defn input [] (d/day-input 2024 16))

(defn parse [input]
  (let [grid  (g/str->grid input)
        start (first (c/keys-when-val #{\S} grid))
        goal  (first (c/keys-when-val #{\E} grid))]
    {:grid (assoc grid goal \.) :start start :goal goal}))

(defn cost-from [from-dir to-dir]
  (cond
    (= from-dir to-dir) 1
    (= from-dir (p/pos- to-dir)) 2001
    :else 1001))

(defn neighbors-fn [grid]
  (fn [[loc dir]]
    (into {}
          (for [d p/orthogonal-dirs
                :let [l (p/pos+ loc d)]
                :when (not= (grid l) \#)]
            [[l d] (cost-from dir d)]))))

(defn goal? [goal]
  (fn [[loc _]] (= loc goal)))

(defn solve [input path-fn]
  (let [{:keys [grid start goal]} (parse input)
        nbrs-map-fn (neighbors-fn grid)
        neighbors (fn [state] (keys (nbrs-map-fn state)))
        cost (fn [state next-state] (get (nbrs-map-fn state) next-state))
        heuristic (fn [[loc _]] (m/manhattan-distance loc goal))]
    (path-fn [start p/dir-e] neighbors (goal? goal) :cost cost :heuristic heuristic)))

(defn part1 [input]
  (solve input path/a-star-cost))

(defn part2 [input]
  (->> (solve input path/dijkstra-all-paths)
       (mapcat #(mapv first %))
       (into #{})
       count))
