;; https://adventofcode.com/2022/day/25
(ns aoc2022.day25
  (:require
   [aoc.day :as d]
   [aoc.util.math :refer [sum]]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2022 25))

(def ^:private snafu-values {\2 2, \1 1, \0 0, \- -1, \= -2})
(def ^:private snafu-digits "=-012")

(defn snafu->int [s]
  (reduce (fn [n d] (+ (* 5 n) (snafu-values d))) 0 s))

(defn int->snafu [n]
  (if (zero? n)
    "0"
    (loop [s '() n n]
      (if (zero? n)
        (str/join s)
        (recur (conj s (nth snafu-digits (mod (+ n 2) 5))) (quot (+ n 2) 5))))))

(defn part1 [input]
  (->> (s/lines input)
       (map snafu->int)
       sum
       int->snafu))

(defn part2 [_] "🎄 Got em all! 🎉")
