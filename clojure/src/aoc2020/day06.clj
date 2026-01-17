;; https://adventofcode.com/2020/day/6
(ns aoc2020.day06
  (:require
   [aoc.day :as d]
   [aoc.util.math :refer [sum]]
   [aoc.util.string :as s]
   [clojure.set :as set]))

(defn input [] (d/day-input 2020 6))

(defn- count-any-yes [block]
  (count (disj (set block) \newline)))

(defn- count-all-yes [block]
  (count (reduce set/intersection (map set (s/lines block)))))

(defn- sum-answered [input answered]
  (->> (s/blocks input)
       (map answered)
       sum))

(defn part1 [input] (sum-answered input count-any-yes))

(defn part2 [input] (sum-answered input count-all-yes))
