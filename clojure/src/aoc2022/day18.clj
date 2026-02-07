;; https://adventofcode.com/2022/day/18
(ns aoc2022.day18
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2022 18))

(defn- parse-lava [input] (set (partition 3 (s/ints input))))

(defn- neighbor-coords [[x y z]]
  [[(inc x) y z] [(dec x) y z]
   [x (inc y) z] [x (dec y) z]
   [x y (inc z)] [x y (dec z)]])

(defn- surface-area [cubes]
  (transduce (mapcat neighbor-coords)
             (completing (fn [faces n] (if (cubes n) faces (inc faces))))
             0
             cubes))

(defn part1 [input] (surface-area (parse-lava input)))

(defn part2 [input]
  (let [lava (parse-lava input)
        [min-x min-y min-z] (map dec (reduce #(map min %1 %2) lava))
        [max-x max-y max-z] (map inc (reduce #(map max %1 %2) lava))
        in-bounds? (fn [[x y z]]
                     (and (<= min-x x max-x)
                          (<= min-y y max-y)
                          (<= min-z z max-z)))]
    (loop [queue (c/queue [min-x min-y min-z])
           seen #{[min-x min-y min-z]}
           faces 0]
      (if-let [current (peek queue)]
        (let [neighbors (neighbor-coords current)
              droplet-neighbors (count (filter lava neighbors))
              new-steam (filter #(and (in-bounds? %)
                                      (not (seen %))
                                      (not (lava %)))
                                neighbors)]
          (recur (into (pop queue) new-steam)
                 (into seen new-steam)
                 (+ faces droplet-neighbors)))
        faces))))
