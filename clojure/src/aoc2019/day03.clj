;; https://adventofcode.com/2019/day/3
(ns aoc2019.day03
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.pos :as p]
   [aoc.util.string :as s]
   [clojure.set :as set]
   [clojure.string :as str]))

(defn input [] (d/day-input 2019 3))

(defn- parse-move [s]
  [(p/dir->offset (first s)) (s/int s)])

(defn- parse-moves [line]
  (map parse-move (str/split line #",")))

(defn- moves->steps [wire]
  (mapcat (fn [[dir mag]] (repeat mag dir)) wire))

(defn- walk-steps
  ([steps] (walk-steps [0 0] steps))
  ([start steps]
   (reductions p/pos+ start steps)))

(defn- parse-wire [line]
  (->> (parse-moves line)
       moves->steps
       walk-steps
       rest
       (map-indexed (fn [i p] [p (inc i)]))
       (reduce (fn [m [p i]] (update m p #(or % i))) {})))

(defn- parse-wires [input]
  (map parse-wire (s/lines input)))

(defn- intersections [wires]
  (apply set/intersection (map (comp set keys) wires)))

(defn part1 [input]
  (->>  (parse-wires input)
        intersections
        (map m/manhattan-distance)
        (apply min)))

(defn part2 [input]
  (let [wires (parse-wires input)]
    (->> (intersections wires)
         (map (fn [p] (reduce + (map #(get % p) wires))))
         (apply min))))
