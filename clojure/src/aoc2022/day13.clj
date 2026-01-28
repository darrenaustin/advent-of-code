;; https://adventofcode.com/2022/day/13
(ns aoc2022.day13
  (:require
   [aoc.day :as d]
   [aoc.util.collection :refer [count-where]]
   [aoc.util.math :refer [product sum]]
   [clojure.edn :as edn]))

(defn input [] (d/day-input 2022 13))

(defn- parse-packets [input]
  (edn/read-string (str "[" input "]")))

(defn- compare-packets [p1 p2]
  (cond
    (and (number? p1) (sequential? p2)) (compare-packets [p1] p2)
    (and (sequential? p1) (number? p2)) (compare-packets p1 [p2])

    (and (sequential? p1) (sequential? p2))
    (let [first-compare (compare-packets (first p1) (first p2))]
      (if (zero? first-compare)
        (compare-packets (next p1) (next p2))
        first-compare))

    :else (compare p1 p2)))

(defn- in-order? [idx [p1 p2]]
  (when (neg? (compare-packets p1 p2))
    (inc idx)))

(defn- packets-before [packet packets]
  (count-where #(not (pos? (compare-packets % packet))) packets))

(defn part1 [input]
  (->> (parse-packets input)
       (partition 2)
       (keep-indexed in-order?)
       sum))

(defn part2 [input]
  (let [packets  (parse-packets input)
        divider1 [[2]]
        divider2 [[6]]]
    (product [(inc (packets-before divider1 packets))
              (+ 2 (packets-before divider2 packets))])))
