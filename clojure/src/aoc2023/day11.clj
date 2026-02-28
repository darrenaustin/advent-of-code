;; https://adventofcode.com/2023/day/11
(ns aoc2023.day11
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.grid :as g]
   [aoc.util.math :as m]))

(defn input [] (d/day-input 2023 11))

(defn- expand [galaxies extent axis expansion]
  (loop [expanded #{}, [coord & coords] (range extent), expand-by 0]
    (if (nil? coord)
      expanded
      (let [values (filter (fn [pos] (= (nth pos axis) coord)) galaxies)]
        (if (empty? values)
          (recur expanded coords (+ expand-by (dec expansion)))
          (let [values' (map (fn [pos] (update pos axis + expand-by)) values)]
            (recur (apply conj expanded values') coords expand-by)))))))

(defn shortest-paths [input expansion]
  (let [grid (g/->sparse-grid input #{\#})
        [w h] (g/size grid)
        galaxies (-> (set (keys grid))
                     (expand w 0 expansion)
                     (expand h 1 expansion))]
    (transduce (map #(apply m/manhattan-distance %)) + 0 (c/pairs galaxies))))

(defn part1 [input] (shortest-paths input 2))

(defn part2 [input] (shortest-paths input 1000000))
