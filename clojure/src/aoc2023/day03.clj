;; https://adventofcode.com/2023/day/3
(ns aoc2023.day03
  (:require
   [aoc.day :as d]
   [aoc.util.math :refer [sum]]
   [aoc.util.string :refer [re-seq-pos]]
   [clojure.string :as str]))

(defn input [] (d/day-input 2023 3))

(defn- parse-schematic [input]
  (let [lines (str/split-lines input)]
    {:numbers (mapcat (fn [[y line]]
                        (for [m (re-seq-pos #"\d+" line)]
                          {:n (parse-long (:match m))
                           :y y
                           :x-range [(:start m) (:end m)]}))
                      (map-indexed vector lines))
     :symbols (into {} (for [[y line] (map-indexed vector lines)
                             m (re-seq-pos #"[^\d.]" line)]
                         [[y (:start m)] (first (:match m))]))}))

(defn- adjacent-to? [y [min-x max-x] [sy sx]]
  (and (<= (dec y) sy (inc y))
       (<= (dec min-x) sx max-x)))

(defn part1 [input]
  (let [{:keys [numbers symbols]} (parse-schematic input)
        sym-locs (keys symbols)]
    (transduce
     (comp
      (filter (fn [{:keys [y x-range]}]
                (some #(adjacent-to? y x-range %) sym-locs)))
      (map :n))
     +
     numbers)))

(defn part2 [input]
  (let [{:keys [numbers symbols]} (parse-schematic input)]
    (sum (for [[pos sym] symbols
               :when (= sym \*)
               :let [neighbors (filter (fn [{:keys [x-range y]}]
                                         (adjacent-to? y x-range pos))
                                       numbers)]
               :when (= 2 (count neighbors))]
           (apply * (map :n neighbors))))))
