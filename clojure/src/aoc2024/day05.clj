;; https://adventofcode.com/2024/day/5
(ns aoc2024.day05
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(def input (d/day-input 2024 5))

(defn parse-input [input]
  (->> (str/split input #"\n\n")
       (map #(map s/parse-nums (str/split-lines %)))))

(defn follows-rules? [order rules]
  (let [order-map (into {} (map-indexed (fn [i e] [e i]) order))]
    (every?
     (fn [[before after]]
       (let [before-order (order-map before)
             after-order (order-map after)]
         (or (nil? before-order) (nil? after-order)
             (< before-order after-order))))
     rules)))

(defn middle [order]
  (nth (vec order) (int (/ (count order) 2))))

(defn rules-compare [rules]
  (let [rules (set rules)]
    (fn [a b]
      (cond
        (contains? rules [a b]) -1
        (contains? rules [b a])  1
        :else                    0))))

(defn part1 [input]
  (let [[rules orders] (parse-input input)]
    (m/sum (map middle (filter #(follows-rules? % rules) orders)))))

(defn part2 [input]
  (let [[rules orders] (parse-input input)
        rules-comparator (rules-compare rules)
        incorrect-orders (filter #(not (follows-rules? % rules)) orders)]
    (m/sum (map middle (map #(sort rules-comparator %) incorrect-orders)))))
