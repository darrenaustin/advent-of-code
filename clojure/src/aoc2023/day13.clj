;; https://adventofcode.com/2023/day/13
(ns aoc2023.day13
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.matrix :as mat]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2023 13))

(defn- differences [as bs]
  (c/count-where false? (map = as bs)))

(defn- horizontal-mirror-at? [rows target-diffs row]
  (let [above (take-last row (rseq rows))
        below (drop row rows)
        diffs (map differences above below)]
    (= target-diffs (reduce + diffs))))

(defn- horizontal-mirror [rows target-diffs]
  (c/first-where
   (partial horizontal-mirror-at? rows target-diffs)
   (range 1 (count rows))))

(defn- mirror-value [target-diffs rows]
  (or (horizontal-mirror (mat/transpose rows) target-diffs)
      (* 100 (horizontal-mirror rows target-diffs))))

(defn- sum-mirrors [input target-diffs]
  (transduce
   (map (comp (partial mirror-value target-diffs) s/lines))
   + 0
   (s/blocks input)))

(defn part1 [input] (sum-mirrors input 0))

(defn part2 [input] (sum-mirrors input 1))
