;; https://adventofcode.com/2017/day/19
(ns aoc2017.day19
  (:require [aoc.day :as d]
            [aoc.util.collection :refer [first-where]]
            [aoc.util.grid :refer :all]))

;; Need to ensure the input isn't trimmed as
;; it will remove important spacing in the grid.
(defn input [] (d/day-input 2017 19 false))

(def turn-dirs
  {dir-down  [dir-left dir-right]
   dir-up    [dir-left dir-right]
   dir-left  [dir-up dir-down]
   dir-right [dir-up dir-down]})

(defn path-letter? [cell]
  (re-find #"[A-Z]" (str cell)))

(defn continuing? [cell]
  (and cell (not= cell \space)))

(defn walk-routes [input]
  (let [grid  (parse-grid input)
        start (first (first-where (fn [[[_ y] c]] (and (zero? y) (= c \|))) grid))]
    (loop [pos start dir dir-down path "" steps 1]
      (let [cell  (grid pos)
            path' (if (path-letter? cell) (str path cell) path)
            pos'  (vec+ pos dir)
            cell' (grid pos')]
        (cond
          (continuing? cell') (recur pos' dir path' (inc steps))
          (not= cell \+) [path' steps]
          :else (let [dir' (first-where #(continuing? (grid (vec+ pos %))) (turn-dirs dir))]
                  (recur (vec+ pos dir') dir' path' (inc steps))))))))

(defn part1 [input] (first (walk-routes input)))

(defn part2 [input] (second (walk-routes input)))
