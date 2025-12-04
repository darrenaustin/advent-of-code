;; https://adventofcode.com/2018/day/11
(ns aoc2018.day11
  (:require [aoc.day :as d]
            [aoc.util.string :as s]
            [clojure.string :as str]))

(defn input [] (d/day-input 2018 11))

(defn power [serial [x y]]
  (let [rack-id (+ x 10)]
    (- (rem (quot (* (+ (* rack-id y) serial) rack-id) 100) 10) 5)))

; Use a summed-area table to make it faster to look up any
; region's sum.
;
; https://en.wikipedia.org/wiki/Summed-area_table
(defn summed-area-grid [[[min-x min-y] [max-x max-y]] val-fn]
  (reduce
   (fn [m [x y]]
     (assoc m [x y]
            (+ (val-fn [x y])
               (or (m [x (dec y)]) 0)
               (or (m [(dec x) y]) 0)
               (- (or (m [(dec x) (dec y)]) 0)))))
   {}
   (for [x (range min-x (inc max-x)) y (range min-y (inc max-y))] [x y])))

(defn square-power [summed-grid [x y] size]
  (let [x0 (dec x) y0 (dec y)
        x1 (+ x size -1) y1 (+ y size -1)]
    (- (+ (or (summed-grid [x0 y0]) 0)
          (or (summed-grid [x1 y1]) 0))
       (or (summed-grid [x0 y1]) 0)
       (or (summed-grid [x1 y0]) 0))))

(defn max-square [summed-grid size]
  (reduce (fn [[mp mc] [p c]] (if (and mp (> mp p)) [mp mc] [p c]))
          nil
          (for [x (range 1 (- 301 size)) y (range 1 (- 301 size))]
            [(square-power summed-grid [x y] size) [x y size]])))

(defn max-3x3 [grid] (max-square grid 3))

(defn max-any-square [grid]
  (loop [size 300 max-power nil max-coords nil]
    (if (and max-power (> max-power (* size size 4)))
      [max-power max-coords]
      (let [[mp mc] (max-square grid size)]
        (if (or (not max-power) (> mp max-power))
          (recur (dec size) mp mc)
          (recur (dec size) max-power max-coords))))))

(defn part1 [input]
  (str/join "," (take 2 (second
                         (max-3x3
                          (summed-area-grid
                           [[1 1] [300 300]]
                           (partial power (s/parse-int input))))))))

(defn part2 [input]
  (str/join "," (second
                 (max-any-square
                  (summed-area-grid
                   [[1 1] [300 300]]
                   (partial power (s/parse-int input)))))))
