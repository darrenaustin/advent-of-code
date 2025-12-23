;; https://adventofcode.com/2019/day/8
(ns aoc2019.day08
  (:require
   [aoc.day :as d]
   [aoc.util.ascii-art :as ascii]
   [aoc.util.collection :as c]
   [clojure.string :as str]))

(defn input [] (d/day-input 2019 8))

(defn part1
  ([input] (part1 input 25 6))
  ([input w h]
   (reduce * (map (->>
                   (partition (* w h) input)
                   (apply min-key #(c/count-where #{\0} %))
                   frequencies)
                  [\1 \2]))))

(defn part2
  ([input] (part2 input 25 6))
  ([input w h]
   (ascii/ocr
    (->> (partition (* w h) input)
         (apply map (fn [& ps] (c/first-where #{\0 \1} ps)))
         (partition w)
         (mapv str/join))
    :on \1)))
