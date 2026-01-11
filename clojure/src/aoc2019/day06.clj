;; https://adventofcode.com/2019/day/6
 (ns aoc2019.day06
   (:require
    [aoc.day :as d]
    [aoc.util.pathfinding :as path]
    [aoc.util.string :as s]
    [clojure.string :as str]))

(defn input [] (d/day-input 2019 6))

(defn- parse-orbit [orbits line]
  (let [[inner outer] (str/split line #"\)")]
    (assoc orbits outer inner)))

(defn- parse-orbits [input]
  (reduce parse-orbit {} (s/lines input)))

(def ^:private num-orbits
  (memoize
   (fn [orbits body]
     (if-let [inner (orbits body)]
       (inc (num-orbits orbits inner))
       0))))

(defn- connect [neighbors [body1 body2]]
  (-> neighbors
      (update body1 conj body2)
      (update body2 conj body1)))

(defn- neighbors-for [orbits]
  (reduce connect {} orbits))

(defn part1 [input]
  (let [orbits (parse-orbits input)]
    (reduce + (map (partial num-orbits orbits) (keys orbits)))))

(defn part2 [input]
  (let [orbits (parse-orbits input)]
    (path/a-star-cost (orbits "YOU")
                      (neighbors-for orbits)
                      #{(orbits "SAN")})))
