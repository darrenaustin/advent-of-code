;; https://adventofcode.com/2020/day/24
(ns aoc2020.day24
  (:require
   [aoc.day :as d]
   [aoc.util.pos :as p]
   [clojure.set :as set]
   [clojure.string :as str]))

(defn input [] (d/day-input 2020 24))

;; Using cube coordinates for the hex grid:
;; https://www.redblobgames.com/grids/hexagons/
(def dirs->cube {"e"  [+1 -1 +0]
                 "se" [+0 -1 +1]
                 "sw" [-1 +0 +1]
                 "w"  [-1 +1 +0]
                 "nw" [+0 +1 -1]
                 "ne" [+1 +0 -1]})

(defn adjacent-tiles [tile]
  (map #(p/pos+ tile %) (vals dirs->cube)))

(defn black-adjacent [black-tiles tile]
  (filter black-tiles (adjacent-tiles tile)))

(defn white-adjacent [black-tiles tile]
  (remove black-tiles (adjacent-tiles tile)))

(defn white-tiles-around [black-tiles]
  (set (apply concat (keep #(white-adjacent black-tiles %) black-tiles))))

(defn parse-tile-dir-paths [input]
  (map #(re-seq #"e|se|sw|w|nw|ne" %) (str/split-lines input)))

(defn parse-tile-coords [input]
  (map #(reduce p/pos+ (map dirs->cube %)) (parse-tile-dir-paths input)))

(defn flip-tile [black-tiles tile]
  (if (contains? black-tiles tile)
    (disj black-tiles tile)
    (conj black-tiles tile)))

(defn parse-black-tiles [input]
  (reduce flip-tile #{} (parse-tile-coords input)))

(defn day-flip [black-tiles]
  (let [black-flipping (filter #(let [adj-black (count (black-adjacent black-tiles %))]
                                  (or (zero? adj-black) (< 2 adj-black)))
                               black-tiles)
        white-flipping (filter #(let [adj-black (count (black-adjacent black-tiles %))]
                                  (= 2 adj-black))
                               (white-tiles-around black-tiles))]
    (-> black-tiles
        (set/difference (set black-flipping))
        (set/union (set white-flipping)))))

(defn part1 [input]
  (count (parse-black-tiles input)))

(defn part2 [input]
  (count (nth (iterate day-flip (parse-black-tiles input)) 100)))
