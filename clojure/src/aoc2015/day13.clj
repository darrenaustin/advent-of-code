;; https://adventofcode.com/2015/day/13
 (ns aoc2015.day13
   (:require
    [aoc.day :as d]
    [aoc.util.collection :as c]
    [aoc.util.math :as m]
    [aoc.util.string :as s]
    [clojure.math.combinatorics :as combo]
    [clojure.set :as set]))

(defn input [] (d/day-input 2015 13))

(defn parse-pair-score [line]
  (let [[_ p1 dir value p2] (re-find #"(\w+) .*(gain|lose) (\d+).* (\w+)." line)
        amount (* (if (= dir "gain") 1 -1) (s/int value))]
    {#{p1 p2} amount}))

(defn pair-scores-map [input]
  (apply merge-with + (map parse-pair-score (s/lines input))))

(defn happiness [pair-scores order]
  (m/sum (keep pair-scores (map set (c/cyclic-adjacent-pairs order)))))

(defn max-happiness [pair-scores people]
  (->> people
       combo/permutations
       (map #(happiness pair-scores %))
       (apply max)))

(defn part1 [input]
  (let [pair-scores (pair-scores-map input)
        people (reduce set/union (keys pair-scores))]
    (max-happiness pair-scores people)))

(defn part2 [input]
  (let [pair-scores (pair-scores-map input)
        people (conj (reduce set/union (keys pair-scores)) "Me")]
    (max-happiness pair-scores people)))
