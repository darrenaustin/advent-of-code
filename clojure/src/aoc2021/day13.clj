;; https://adventofcode.com/2021/day/13
 (ns aoc2021.day13
   (:require
    [aoc.day :as d]
    [aoc.util.ascii-art :as ascii]
    [aoc.util.sparse-grid :as sg]
    [aoc.util.string :as s]
    [clojure.string :as str]))

(defn input [] (d/day-input 2021 13))

(defn- parse-dots [input]
  (let [[dots _] (s/blocks input)]
    (into (sg/make-sparse-grid)
          (map (fn [ds] [(s/ints ds) \#])
               (s/lines dots)))))

(defn- fold-axis [axis n]
  (if (< n axis) n (- (* 2 axis) n)))

(defn fold-at-axis [axis-idx axis grid]
  (into (sg/make-sparse-grid)
        (map (fn [[pos v]]
               [(update pos axis-idx #(fold-axis axis %)) v])
             grid)))

(defn- parse-folds [input]
  (let [[_ folds] (s/blocks input)]
    (map #(if (str/starts-with? % "fold along x")
            (partial fold-at-axis 0 (s/int %))
            (partial fold-at-axis 1 (s/int %)))
         (s/lines folds))))

(defn part1 [input]
  (count ((first (parse-folds input)) (parse-dots input))))

(defn part2 [input]
  (ascii/ocr
   (sg/format-rows
    (reduce #(%2 %1) (parse-dots input) (parse-folds input)))))
