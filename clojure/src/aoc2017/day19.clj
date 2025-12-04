;; https://adventofcode.com/2017/day/19
(ns aoc2017.day19
  (:require [aoc.day :as d]
            [aoc.util.collection :refer [first-where]]
            [aoc.util.grid :as g]
            [aoc.util.vec :as v]))

;; Need to ensure the input isn't trimmed as
;; it will remove important spacing in the grid.
(defn input [] (d/day-input 2017 19 false))

(def turn-dirs
  {v/dir-down  [v/dir-left v/dir-right]
   v/dir-up    [v/dir-left v/dir-right]
   v/dir-left  [v/dir-up v/dir-down]
   v/dir-right [v/dir-up v/dir-down]})

(defn path-letter? [cell]
  (re-find #"[A-Z]" (str cell)))

(defn continuing? [cell]
  (and cell (not= cell \space)))

(defn walk-routes [input]
  (let [grid  (g/parse-grid input)
        start (first (first-where (fn [[[_ y] c]] (and (zero? y) (= c \|))) grid))]
    (loop [pos start dir v/dir-down path "" steps 1]
      (let [cell  (grid pos)
            path' (if (path-letter? cell) (str path cell) path)
            pos'  (v/vec+ pos dir)
            cell' (grid pos')]
        (cond
          (continuing? cell') (recur pos' dir path' (inc steps))
          (not= cell \+) [path' steps]
          :else (let [dir' (first-where #(continuing? (grid (v/vec+ pos %))) (turn-dirs dir))]
                  (recur (v/vec+ pos dir') dir' path' (inc steps))))))))

(defn part1 [input] (first (walk-routes input)))

(defn part2 [input] (second (walk-routes input)))
