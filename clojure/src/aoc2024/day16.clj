;; https://adventofcode.com/2024/day/16
(ns aoc2024.day16
  (:require
   [aoc.day :as d]
   [aoc.util.grid :as g]
   [aoc.util.pathfinding :as path]
   [aoc.util.vec :as v]))

(defn input [] (d/day-input 2024 16))

(defn parse [input]
  (let [grid  (g/parse-grid input)
        start (g/loc-where grid #{\S})
        goal  (g/loc-where grid #{\E})]
    {:grid (assoc grid goal \.) :start start :goal goal}))

(defn cost-from [from-dir to-dir]
  (cond
    (= from-dir to-dir) 1
    (= from-dir (v/vec- to-dir)) 2001
    :else 1001))

(defn neighbors-fn [grid]
  (fn [[loc dir]]
    (into {}
          (for [d v/orthogonal-dirs
                :let [l (v/vec+ loc d)]
                :when (not= (grid l) \#)]
            [[l d] (cost-from dir d)]))))

(defn goal? [goal]
  (fn [[loc _]] (= loc goal)))

(defn solve [input path-fn]
  (let [{:keys [grid start goal]} (parse input)]
    (path-fn [start v/dir-e] (neighbors-fn grid) (goal? goal))))

(defn part1 [input]
  (solve input path/dijkstra-distance))

(defn part2 [input]
  (->> (solve input path/dijkstra-paths)
       (mapcat #(mapv first %))
       (into #{})
       count))
