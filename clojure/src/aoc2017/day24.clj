;; https://adventofcode.com/2017/day/24
(ns aoc2017.day24
  (:require [aoc.day :as d]
            [aoc.util.math :as m]
            [aoc.util.string :as s]))

(defn input [] (d/day-input 2017 24))

(defn parse [input]
  (reduce (fn [m c] (reduce (fn [m p] (assoc m p (conj (get m p #{}) c))) m c))
          {}
          (partition 2 (s/parse-ints input))))

(defn bridges
  ([components] (bridges 0 #{} components))
  ([start visited components]
   (let [options (remove visited (components start))]
     (if (empty? options)
       [[]]
       (mapcat
        (fn [component]
          (let [[port1 port2] component
                start' (if (= port1 start) port2 port1)]
            (map #(concat [component] %)
                 (bridges start' (conj visited component) components))))
        options)))))

(defn strength [bridge]
  (m/sum (flatten bridge)))

(defn part1 [input]
  (->> input
       parse
       bridges
       (map strength)
       (apply max)))

(defn part2 [input]
  (->> input
       parse
       bridges
       (m/maxes-by count)
       second
       (map strength)
       (apply max)))
