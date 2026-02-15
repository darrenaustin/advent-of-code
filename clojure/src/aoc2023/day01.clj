;; https://adventofcode.com/2023/day/1
(ns aoc2023.day01
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m :refer [sum]]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2023 1))

(def ^:private digit-names
  ["zero" "one" "two" "three" "four" "five" "six" "seven" "eight" "nine"])

(def ^:private word->digit
  (reduce-kv (fn [m i w] (assoc m w i (str i) i)) {} digit-names))

(def ^:private word-digit-regex
  (re-pattern (str/join "|" (conj digit-names "\\d"))))

(defn- calibration-value [line]
  (->> (re-seq #"\d" line)
       ((juxt first last))
       str/join
       s/int))

(defn- word-calibration-value [line]
  (->> (s/re-seq-overlapping word-digit-regex line)
       ((juxt first last))
       (map word->digit)
       str/join
       s/int))

(defn- calibration-sum [input calib-fn]
  (->> (s/lines input)
       (map calib-fn)
       sum))

(defn part1 [input] (calibration-sum input calibration-value))

(defn part2 [input] (calibration-sum input word-calibration-value))
