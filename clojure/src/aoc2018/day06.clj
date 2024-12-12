;; https://adventofcode.com/2018/day/6
(ns aoc2018.day06
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(def input (d/day-input 2018 6))

(defn parse-coords [input]
  (->> input
       str/split-lines
       (map s/parse-nums)))

(defn closest-to [pos coords]
  (let [[_ min-coords]
        (reduce
         (fn [[min-dist min-coords] coord]
           (let [dist (m/manhatten-distance pos coord)]
             (cond
               (< dist min-dist) [dist [coord]]
               (= dist min-dist) [dist (conj min-coords coord)]
               :else [min-dist min-coords])))
         [Integer/MAX_VALUE nil]
         coords)]
    (when (= 1 (count min-coords))
      (first min-coords))))

(defn map-bounds [coords]
  (reduce
   (fn [[min-x min-y max-x max-y] [x y]]
     [(min min-x x) (min min-y y) (max max-x x) (max max-y y)])
   [Integer/MAX_VALUE Integer/MAX_VALUE 0 0]
   coords))

(defn map-positions [[min-x min-y max-x max-y]]
  (for [x (range min-x (inc max-x))
        y (range min-y (inc max-y))]
    [x y]))

(defn area-map [coords bounds]
  (reduce
   (fn [closest pos]
     (let [coord (closest-to pos coords)]
       (if coord
         (update closest coord conj pos)
         closest)))
   {}
   (map-positions bounds)))

(defn on-edge? [[x y] [min-x min-y max-x max-y]]
   (or (= x min-x) (= x max-x)
      (= y min-y) (= y max-y)))

(defn infinite-area? [area-pos bounds]
  (some #(on-edge? % bounds) area-pos))

(defn finite-areas [area-map bounds]
  (into {}
        (filter (fn [[_ area]] (not (infinite-area? area bounds)))
                area-map)))

(defn part1 [input]
  (let [coords (parse-coords input)
        bounds (map-bounds coords)
        areas (finite-areas (area-map coords bounds) bounds)]
    (apply max (map count (vals areas)))))

(defn within-dist? [pos coords dist]
  (reduce (fn [dist-sum coord]
            (let [coord-dist (m/manhatten-distance pos coord)
                  new-sum (+ dist-sum coord-dist)]
              (if (< new-sum dist)
                new-sum
                (reduced nil))))
          0
          coords))

(defn part2
  ([input] (part2 input 10000))
  ([input dist]
   (let [coords (parse-coords input)
         bounds (map-bounds coords)]
     (count (filter #(within-dist? % coords dist) (map-positions bounds))))))
