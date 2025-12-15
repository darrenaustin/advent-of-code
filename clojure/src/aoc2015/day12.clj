;; https://adventofcode.com/2015/day/12
(ns aoc2015.day12
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [clojure.data.json :as json]))

(defn input [] (d/day-input 2015 12))

(defn sum-except [pred o]
  (cond
    (pred o) 0
    (number? o) o
    (vector? o) (m/sum (map #(sum-except pred %) o))
    (map? o) (m/sum (map #(sum-except pred %) (vals o)))
    :else 0))

(defn red-map? [o]
  (and (map? o) (some #{"red"} (vals o))))

(defn part1 [input] (m/sum (s/ints input)))

(defn part2 [input]
  (sum-except red-map? (json/read-str input)))
