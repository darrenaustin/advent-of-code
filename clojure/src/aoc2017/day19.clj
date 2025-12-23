;; https://adventofcode.com/2017/day/19
(ns aoc2017.day19
  (:require
   [aoc.day :as d]
   [aoc.util.collection :refer [first-where]]
   [aoc.util.grid-vec :as g]
   [aoc.util.pos :as p]))

(defn input [] (d/day-input 2017 19 :trim? false))

(def turn-dirs
  {p/dir-down  [p/dir-left p/dir-right]
   p/dir-up    [p/dir-left p/dir-right]
   p/dir-left  [p/dir-up p/dir-down]
   p/dir-right [p/dir-up p/dir-down]})

(defn path-letter? [cell]
  (re-find #"[A-Z]" (str cell)))

(defn continuing? [cell]
  (and cell (not= cell \space)))

(defn walk-routes [input]
  (let [grid  (g/str->grid-vec input)
        start (first (first-where (fn [[[_ y] c]] (and (zero? y) (= c \|))) grid))]
    (loop [pos start, dir p/dir-down, path "", steps 1]
      (let [cell  (grid pos)
            path' (if (path-letter? cell) (str path cell) path)
            pos'  (p/pos+ pos dir)
            cell' (grid pos')]
        (cond
          (continuing? cell') (recur pos' dir path' (inc steps))
          (not= cell \+) [path' steps]
          :else (let [dir' (first-where #(continuing? (grid (p/pos+ pos %))) (turn-dirs dir))]
                  (recur (p/pos+ pos dir') dir' path' (inc steps))))))))

(defn part1 [input] (first (walk-routes input)))

(defn part2 [input] (second (walk-routes input)))
