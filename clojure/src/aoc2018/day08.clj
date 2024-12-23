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

(defn parse-tree [input]
  (first (parse-node (s/parse-ints input))))

(defn metadata [t]
  (concat (mapcat metadata (:children t)) (:metadata t)))

(defn value [t]
  (if (empty? (:children t))
    (m/sum (:metadata t))
    (m/sum (map value (for [m (:metadata t)
                            :when (<= 1 m (count (:children t)))]
                        (get (:children t) (dec m)))))))

(defn part1 [input]
  (-> input parse-tree metadata m/sum))

(defn part2 [input]
  (-> input parse-tree value))
