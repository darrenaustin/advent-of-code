;; https://adventofcode.com/2016/day/1
(ns aoc2016.day01
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.math :as m]
   [aoc.util.matrix :as mat]
   [aoc.util.pos :as p]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2016 1))

(def turns {\R p/turn-right, \L p/turn-left})

(defn parse-moves [input]
  (map (fn [dn] [(turns (first dn)) (s/int dn)])
       (str/split input #", ")))

(defn move [[pos dir] [turn dist]]
  (let [new-dir (turn dir)]
    [(p/pos+ pos (p/pos* dist new-dir)) new-dir]))

(defn moves->path [moves start]
  (map first (reductions move start moves)))

(defn moves->steps [moves [pos dir]]
  (let [[turns dists] (mat/transpose moves)
        dirs (rest (reductions (fn [d turn] (turn d)) dir turns))
        deltas (mapcat repeat dists dirs)]
    (rest (reductions p/pos+ pos deltas))))

(defn part1 [input]
  (-> (parse-moves input)
      (moves->path [p/origin p/dir-n])
      last
      (m/manhattan-distance p/origin)))

(defn part2 [input]
  (-> (parse-moves input)
      (moves->steps [p/origin p/dir-n])
      c/first-duplicate
      (m/manhattan-distance p/origin)))
