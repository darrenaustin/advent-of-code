;; https://adventofcode.com/2019/day/4
(ns aoc2019.day04
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2019 4))

(defn- valid-password1? [n]
  (let [ds (m/digits n)]
    (and (apply <= ds)
         (some #(<= 2 (count %)) (partition-by identity ds)))))

(defn- valid-password2? [n]
  (let [ds (m/digits n)]
    (and (apply <= ds)
         (some #(= 2 (count %)) (partition-by identity ds)))))

(defn- num-valid [input valid?]
  (->> (s/pos-ints input)
       (apply m/range-inc)
       (filter valid?)
       count))

(defn part1 [input] (num-valid input valid-password1?))

(defn part2 [input] (num-valid input valid-password2?))
