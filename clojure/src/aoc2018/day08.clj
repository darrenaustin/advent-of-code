;; https://adventofcode.com/2018/day/8
(ns aoc2018.day08
  (:require [aoc.day :as d]
            [aoc.util.math :as m]
            [aoc.util.string :as s]))

(defn input [] (d/day-input 2018 8))

(declare parse-node)

(defn- parse-children [num-children ns]
  (reduce
    (fn [[cs ns] _]
      (let [[c ns'] (parse-node ns)]
        [(conj cs c) ns']))
    [[] ns]
    (range num-children)))

(defn- parse-node [ns]
  (let [[num-child num-meta & rest] ns
        [cs ns'] (parse-children num-child rest)]
    [{:children cs
      :metadata (take num-meta ns')}
     (drop num-meta ns')]))

(defn parse-tree [ns]
  (first (parse-node ns)))

(defn metadata [t]
  (concat (mapcat metadata (:children t)) (:metadata t)))

(defn value [t]
  (if (empty? (:children t))
    (m/sum (:metadata t))
    (m/sum (map value (for [m (:metadata t)
                            :when (<= 1 m (count (:children t)))]
                        (get (:children t) (dec m)))))))

(defn part1 [input]
  (m/sum (metadata (parse-tree (s/parse-ints input)))))

(defn part2 [input]
  (value (parse-tree (s/parse-ints input))))

(def t "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2")
