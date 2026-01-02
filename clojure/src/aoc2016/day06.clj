;; https://adventofcode.com/2016/day/6
(ns aoc2016.day06
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2016 6))

(defn- most-common [xs]
  (key (apply max-key val (frequencies xs))))

(defn- least-common [xs]
  (key (apply min-key val (frequencies xs))))

(defn- error-corrected [f input]
  (->> (s/lines input)
       (apply map vector)
       (map f)
       str/join))

(defn part1 [input]
  (error-corrected most-common input))

(defn part2 [input]
  (error-corrected least-common input))
