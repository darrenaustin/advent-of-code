;; https://adventofcode.com/2016/day/9
(ns aoc2016.day09
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2016 9))

(defn- parse-marker [s]
  (when-let [[match prefix chrs times] (re-find #"(.*?)\((\d+)x(\d+)\)" s)]
    [prefix (s/int chrs) (s/int times) (subs s (count match))]))

(defn- length-v1 [compressed]
  (loop [s compressed, result 0]
    (if-let [[prefix chrs times suffix] (parse-marker s)]
      (recur (subs suffix chrs)
             (+ result (count prefix) (* chrs times)))
      (+ result (count s)))))

(defn- length-v2 [compressed]
  (if-let [[prefix chrs times suffix] (parse-marker compressed)]
    (+ (count prefix)
       (* times (length-v2 (subs suffix 0 chrs)))
       (length-v2 (subs suffix chrs)))
    (count compressed)))

(defn part1 [input] (length-v1 input))

(defn part2 [input] (length-v2 input))
