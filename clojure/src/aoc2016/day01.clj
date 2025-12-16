;; https://adventofcode.com/2016/day/1
(ns aoc2016.day01
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.pos :as p]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2016 1))

(def dirs {\R p/turn-right, \L p/turn-left})

(defn parse-directions [input]
  (map (fn [dn] [(dirs (first dn)) (s/int dn)])
       (str/split input #", ")))

(defn move [[pos dir] [turn dist]]
  (let [new-dir (turn dir)]
    [(p/pos+ pos (p/pos* dist new-dir)) new-dir]))

(defn move-path [[pos dir] [turn dist]]
  (let [new-dir (turn dir)
        path (take dist (rest (iterate (partial p/pos+ new-dir) pos)))]
    [[(last path) new-dir] path]))

(defn part1 [input]
  (->> (parse-directions input)
       (reduce move [[0 0] p/dir-up])
       first
       (m/manhattan-distance [0 0])))

(defn part2 [input]
  (loop [current [[0 0] p/dir-up], visited #{} moves (parse-directions input)]
    (when-let [move (first moves)]
      (let [[current' path] (move-path current move)
            already-seen (first (filter visited path))]
        (if already-seen
          (m/manhattan-distance [0 0] already-seen)
          (recur current' (apply conj visited path) (rest moves)))))))
