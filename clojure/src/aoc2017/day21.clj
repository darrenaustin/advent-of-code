; https://adventofcode.com/2017/day/21
(ns aoc2017.day21
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2017 21))

(defn image->grid [image]
  (map vec (str/split image #"/")))

(defn permutations [grid]
  (let [rotations (take 4 (iterate c/rotate-right grid))]
    (distinct (concat (map c/flip-horizontal rotations) rotations))))

(defn expand-rule [rules [pattern replacement]]
  (into rules (for [p (permutations pattern)] [p replacement])))

(defn parse [input]
  (->> input
       s/lines
       (map #(str/split % #" => "))
       (map #(map image->grid %))
       (reduce expand-rule {})))

(def start-grid (image->grid ".#./..#/###"))

(defn sub-divide [grid]
  (let [sub-size (if (even? (count grid)) 2 3)]
    (map #(map c/transpose (partition sub-size (c/transpose %)))
         (partition sub-size grid))))

(defn super-grid [grid]
  (mapcat #(map flatten (c/transpose %)) grid))

(defn enhance [rules grid]
  (if (#{2 3} (count grid))
    (rules grid)
    (super-grid (map #(mapv rules %) (sub-divide grid)))))

(defn on-pixels [grid]
  (c/count-where #{\#} (flatten (map flatten grid))))

(defn on-after-iterations [input iterations]
  (->> iterations
       (nth (iterate (partial enhance (parse input)) start-grid))
       on-pixels))

(defn part1 [input] (on-after-iterations input 5))

(defn part2 [input] (on-after-iterations input 18))
