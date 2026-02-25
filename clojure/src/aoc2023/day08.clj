;; https://adventofcode.com/2023/day/8
(ns aoc2023.day08
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2023 8))

(defn- parse-network [block]
  (->> (re-seq #"\w{3}" block)
       (partition 3)
       (map (fn [[x l r]] {x {\L l \R r}}))
       (apply merge)))

(defn- steps [from goal? dirs network]
  (loop [steps 0, current from, [dir & dirs] (cycle dirs)]
    (if (goal? current)
      steps
      (recur (inc steps) (get-in network [current dir]) dirs))))

(defn part1 [input]
  (let [[dirs network] (s/parse-blocks input [identity parse-network])]
    (steps "AAA" #{"ZZZ"} dirs network)))

(defn part2 [input]
  (let [[dirs network] (s/parse-blocks input [identity parse-network])]
    (->> (filter #(str/ends-with? % "A") (keys network))
         (map (fn [s] (steps s #(str/ends-with? % "Z") dirs network)))
         (reduce m/lcm))))
