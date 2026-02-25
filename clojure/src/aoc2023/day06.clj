;; https://adventofcode.com/2023/day/6
(ns aoc2023.day06
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [clojure.math :as math]
   [clojure.string :as str]))

(defn input [] (d/day-input 2023 6))

(defn- parse-races [input]
  (->> (s/lines input)
       (map s/ints)
       (apply map vector)))

(defn- ways-to-beat [duration record]
  (let [[press-time1 press-time2] (m/quadratic-roots 1 (- duration) record)]
    (int (dec (- (math/ceil press-time1) (math/floor press-time2))))))

(defn- record-beats-product [input]
  (->> (parse-races input)
       (map #(apply ways-to-beat %))
       (reduce *)))

(defn part1 [input] (record-beats-product input))

(defn part2 [input] (record-beats-product (str/replace input #" " "")))
