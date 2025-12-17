;; https://adventofcode.com/2023/day/1
(ns aoc2023.day01
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2023 1))

(def digit-names ["zero" "one" "two" "three" "four"
                  "five" "six" "seven" "eight" "nine"])

(def word->digit
  (reduce-kv (fn [m i w] (assoc m w i (str i) i)) {} digit-names))

(def word-digit-regex
  (re-pattern (str/join "|" (conj digit-names #"\d"))))

(defn calibration-value [line]
  (->> (re-seq #"\d" line)
       ((juxt first last))
       str/join
       s/int))

(defn word-calibration-value [line]
  (->> (s/re-seq-overlapping word-digit-regex line)
       ((juxt first last))
       (map word->digit)
       str/join
       s/int))

(defn part1 [input]
  (->> input
       s/lines
       (map calibration-value)
       m/sum))

(defn part2 [input]
  (->> input
       s/lines
       (map word-calibration-value)
       m/sum))
