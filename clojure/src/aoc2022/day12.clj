;; https://adventofcode.com/2022/day/12
(ns aoc2022.day12
  (:require
   [aoc.day :as d]
   [aoc.util.collection :refer [keys-when-val]]
   [aoc.util.grid :as g]
   [aoc.util.pathfinding :refer [a-star-cost]]
   [aoc.util.pos :as p]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2022 12))

(def ^:private chr->height
  (merge (zipmap s/alphabet-lower (range))
         {\S 0, \E 25}))

(defn- parse-map [input]
  (let [grid (g/->grid input)]
    {:grid  (update-vals grid chr->height)
     :start (first (keys-when-val #{\S} grid))
     :dest  (first (keys-when-val #{\E} grid))}))

(defn- neighbors [grid & {:keys [down?] :or {down? false}}]
  (fn [pos]
    (let [height (grid pos)]
      (filter #(when-let [n-height (grid %)]
                 (if down?
                   (<= (dec height) n-height)
                   (<= n-height (inc height))))
              (p/orthogonal-to pos)))))

(defn part1 [input]
  (let [{:keys [grid start dest]} (parse-map input)]
    (a-star-cost start (neighbors grid) #{dest})))

(defn part2 [input]
  (let [{:keys [grid dest]} (parse-map input)]
    (a-star-cost dest
                 (neighbors grid :down? true)
                 #(zero? (grid %)))))
