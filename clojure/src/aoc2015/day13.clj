;; https://adventofcode.com/2015/day/13
(ns aoc2015.day13
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [clojure.math.combinatorics :as combo]
   [clojure.set :as set]))

(defn input [] (d/day-input 2015 13))

(defn parse-rule [line]
  (let [[_ p1 dir value p2] (re-find #"(\w+) .*(gain|lose) (\d+).* (\w+)." line)
        amount (* (if (= dir "gain") 1 -1) (s/int value))]
    [#{p1 p2} amount]))

(defn parse-rules [input]
  (reduce (fn [m [k n]]
            (update m k (fnil (partial + n) 0)))
          {} (map parse-rule (s/lines input))))

(defn happiness [rules order]
  (+ (m/sum (map rules (map set (partition 2 1 order))))
     (rules #{(first order) (last order)})))

(defn part1 [input]
  (let [rules (parse-rules input)
        people (reduce set/union (keys rules))
        arrangements (combo/permutations people)]
    (apply max (map #(happiness rules %) arrangements))))

(defn part2 [input]
  (let [rules (parse-rules input)
        people (reduce set/union (keys rules))
        rules (into rules (map (fn [p] [#{"Me" p} 0]) people))
        arrangements (combo/permutations (conj people "Me"))]
    (apply max (map #(happiness rules %) arrangements))))
