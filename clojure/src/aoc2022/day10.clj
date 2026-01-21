;; https://adventofcode.com/2022/day/10
 (ns aoc2022.day10
   (:require
    [aoc.day :as d]
    [aoc.util.ascii-art :as ascii]
    [aoc.util.string :as s]
    [clojure.string :as str]))

(defn input [] (d/day-input 2022 10))

(defn- x-series [input]
  (->> (s/lines input)
       (mapcat #(if (= % "noop") [0] [0 (s/int %)]))
       (reductions + 1)
       vec))

(defn- pixel-at [cycle-idx x-reg]
  (if (<= (dec x-reg) (mod cycle-idx 40) (inc x-reg)) \# \.))

(defn part1 [input]
  (let [xs (x-series input)]
    (->> [20 60 100 140 180 220]
         (map #(* % (xs (dec %))))
         (reduce +))))

(defn part2 [input]
  (->> (x-series input)
       (map-indexed pixel-at)
       (partition 40)
       (map str/join)
       ascii/ocr))
