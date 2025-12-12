;; https://adventofcode.com/2018/day/8
(ns aoc2018.day08
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2018 8))

(declare parse-tree)

(defn- parse-children [num-children ns]
  (loop [num-children num-children ns ns children []]
    (if (zero? num-children)
      children
      (let [child (parse-tree ns)]
        (recur (dec num-children)
               (drop (:size child) ns)
               (conj children child))))))

(defn parse-tree [ns]
  (let [[num-child num-meta & payload] ns
        cs      (parse-children num-child payload)
        cs-size (m/sum (map :size cs))
        size    (+ 2 cs-size num-meta)]
    {:size     size
     :children cs
     :metadata (take num-meta (drop cs-size payload))}))

(defn metadata [t]
  (concat (mapcat metadata (:children t)) (:metadata t)))

(defn value [t]
  (if (empty? (:children t))
    (m/sum (:metadata t))
    (m/sum (map value (for [m (:metadata t)]
                        (get (:children t) (dec m) 0))))))

(defn part1 [input]
  (-> input s/parse-ints parse-tree metadata m/sum))

(defn part2 [input]
  (-> input s/parse-ints parse-tree value))
