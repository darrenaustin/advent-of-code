;; https://adventofcode.com/2023/day/2
(ns aoc2023.day02
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2023 2))

(defn- parse-game [line]
  (reduce (fn [acc [_ n color]] (update acc (keyword color) max (s/int n)))
          {:id (s/int line) :red 0 :green 0 :blue 0}
          (re-seq #"(\d+) (red|green|blue)" line)))

(defn- possible? [bag game]
  (every? (fn [[color limit]] (<= (game color) limit)) bag))

(defn- power [game] (* (:red game) (:green game) (:blue game)))

(defn- sum-of [xform input]
  (transduce xform + 0 (s/lines input)))

(defn part1 [input]
  (sum-of (comp (map parse-game)
                (filter #(possible? {:red 12 :green 13 :blue 14} %))
                (map :id))
          input))

(defn part2 [input]
  (sum-of (map (comp power parse-game)) input))
