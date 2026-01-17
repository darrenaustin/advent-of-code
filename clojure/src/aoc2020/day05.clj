;; https://adventofcode.com/2020/day/5
(ns aoc2020.day05
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2020 5))

(defn seat-id [code]
  (-> code
      (str/escape {\F \0 \B \1 \L \0 \R \1})
      (s/int 2)))

(defn part1 [input]
  (apply max (map seat-id (s/lines input))))

(defn part2 [input]
  (->> (s/lines input)
       (map seat-id)
       sort
       (partition 2 1)
       (some (fn [[a b]] (when (not= (inc a) b) (inc a))))))
