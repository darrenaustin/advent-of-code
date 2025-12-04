;; https://adventofcode.com/2024/day/12
(ns aoc2024.day12
  (:require [aoc.day :as d]
            [aoc.util.grid :as g]
            [aoc.util.math :as m]
            [aoc.util.vec :as v]))

(defn input [] (d/day-input 2024 12))

(defn edge-perimeter [region]
  (m/sum (map #(count (remove region (v/orthogonal-from %1))) region)))

(defn fence-cost [region]
  (* (count region) (edge-perimeter region)))

(defn horiz-sides-for [locs row]
  (nth (reduce (fn ([[prev-top prev-bottom sides] [x y]]
                    (let [top    (and (locs [x y]) (not (locs [x (dec y)])))
                          bottom (and (locs [x y]) (not (locs [x (inc y)])))]
                      [top bottom
                       (+ (if (and top (not prev-top)) 1 0)
                          (if (and bottom (not prev-bottom)) 1 0)
                          sides)])))
               [false false 0]
               row) 2))

(defn vert-sides-for [locs col]
  (nth (reduce (fn ([[prev-left prev-right sides] [x y]]
                    (let [left  (and (locs [x y]) (not (locs [(dec x) y])))
                          right (and (locs [x y]) (not (locs [(inc x) y])))]
                      [left right
                       (+ (if (and left (not prev-left)) 1 0)
                          (if (and right (not prev-right)) 1 0)
                          sides)])))
               [false false 0]
               col) 2))

(defn sides [region]
  (let [[[min-x min-y] [max-x max-y]] (g/area-bounds region)]
    (apply + (concat (map #(horiz-sides-for region (for [x (range min-x (inc max-x))] [x %]))
                          (range min-y (inc max-y)))
                     (map #(vert-sides-for region (for [y (range min-y (inc max-y))] [% y]))
                          (range min-x (inc max-x)))))))

(defn fence-cost2 [locs]
  (* (count locs) (sides locs)))

(defn like-neighbors [grid loc]
  (let [plant (grid loc)]
    (filter #(= (grid %) plant) (v/orthogonal-from loc))))

(defn connected-to [grid loc]
  (loop [region #{loc}
         open   (like-neighbors grid loc)]
    (if-let [n (first open)]
      (if (region n)
        (recur region (rest open))
        (recur (conj region n)
               (concat (rest open) (like-neighbors grid n))))
      region)))

(defn regions [grid]
  (loop [rs   []
         locs (set (keys grid))]
    (if-let [loc (first locs)]
      (let [connected (connected-to grid loc)]
        (recur (conj rs connected)
               (apply disj locs connected)))
      rs)))

(defn solve [input cost-fn]
  (->> input
       g/parse-grid
       regions
       (map cost-fn)
       m/sum))

(defn part1 [input] (solve input fence-cost))

(defn part2 [input] (solve input fence-cost2))
