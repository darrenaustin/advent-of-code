;; https://adventofcode.com/2020/day/2
(ns aoc2020.day02
  (:require
   [aoc.day :as d]
   [aoc.util.collection :refer [count-where]]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2020 2))

(defn- parse-entry [line]
  (let [[_ x y chr password] (re-find #"^(\d+)-(\d+) (\w): (\w+)$" line)]
    [(s/int x) (s/int y) (first chr) password]))

(defn- freq-policy? [[min-occ max-occ chr password]]
  (<= min-occ (count-where #{chr} password) max-occ))

(defn- pos-policy? [[pos1 pos2 chr password]]
  (let [in-pos1? (= (get password (dec pos1)) chr)
        in-pos2? (= (get password (dec pos2)) chr)]
    (not= in-pos1? in-pos2?)))

(defn- valid-passwords [input policy?]
  (count-where policy? (map parse-entry (s/lines input))))

(defn part1 [input] (valid-passwords input freq-policy?))

(defn part2 [input] (valid-passwords input pos-policy?))
