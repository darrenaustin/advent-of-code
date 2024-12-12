;; https://adventofcode.com/2024/day/7
(ns aoc2024.day07
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [clojure.edn :as edn]
   [clojure.string :as str]))

(def input (d/day-input 2024 7))

(defn parse-equations [input]
  (->> input
       str/split-lines
       (map s/parse-nums)))

(defn possible-equation? [[goal & nums] ops]
  (loop [values [(first nums)] nums (rest nums)]
    (if-let [x (first nums)]
      (let [next-values (filter #(<= % goal)
                                (mapcat (fn [v] (map (fn [op] (op v x)) ops))
                                        values))]
        (if (seq next-values)
          (recur next-values (rest nums))
          false))
      (some #(= goal %) values))))

(defn || [a b]
  (edn/read-string (str a b)))

(defn part1 [input]
  (->> input
       parse-equations
       (filter #(possible-equation? % [+ *]))
       (map first)
       m/sum))

;; TODO: speed up
(defn part2 [input]
  (->> input
      parse-equations
      (filter #(possible-equation? % [+ * ||]))
      (map first)
      m/sum))
