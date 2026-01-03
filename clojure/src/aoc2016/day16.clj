;; https://adventofcode.com/2016/day/16
(ns aoc2016.day16
  (:require
   [aoc.day :as d]
   [clojure.string :as str]))

(defn input [] (d/day-input 2016 16))

(defn- expand [s n]
  (if (<= n (count s))
    (subs s 0 n)
    (recur (str s "0" (str/join (map {\0 \1, \1 \0} (reverse s)))) n)))

(defn- checksum [s]
  (if (odd? (count s))
    (str/join s)
    (recur (mapv #(if (apply = %) \1 \0) (partition 2 s)))))

(defn part1
  ([input] (part1 input 272))
  ([input size] (checksum (expand input size))))

(defn part2 [input] (checksum (expand input 35651584)))
