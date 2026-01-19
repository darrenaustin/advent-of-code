;; https://adventofcode.com/2020/day/13
(ns aoc2020.day13
  (:require
   [aoc.day :as d]
   [aoc.util.math :refer [chinese-remainder min-by product]]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2020 13))

(defn- wait-time [depart bus]
  [bus (mod (- depart) bus)])

(defn part1 [input]
  (let [[depart & buses] (s/ints input)]
    (->> buses
         (map (partial wait-time depart))
         (min-by second)
         product)))

(defn part2 [input]
  (->> (second (s/lines input))
       (#(str/split % #","))
       (keep-indexed (fn [i b]
                       (when-let [bus (s/int b)]
                         [i bus])))
       (map (juxt (comp - first) second))
       (apply map vector)
       (apply chinese-remainder)))
