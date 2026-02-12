(ns aoc2022.day22
  ;; https://adventofcode.com/2022/day/22
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.grid :as g]
   [aoc.util.pos :as p]
   [aoc.util.string :as s]
   [clojure.core.match :refer [match]]))

(defn input [] (d/day-input 2022 22 :trim? false))

(defn- turn-left [[dx dy]] [dy (- dx)])
(defn- turn-right [[dx dy]] [(- dy) dx])

(def ^:private dirs [p/dir-right p/dir-down p/dir-left p/dir-up])

(defn- parse-path [s]
  (map (fn [[move _ _]]
         (condp = move
           "L" turn-left
           "R" turn-right
           (s/int move)))
       (re-seq #"(\d+)|(L|R)" s)))

(defn- wrap-torus [grid pos dir]
  (loop [current pos]
    (let [pos' (p/pos- current dir)]
      (if (contains? grid pos')
        (recur pos')
        [current dir]))))

(defn- wrap-cube [_ [x y] dir]
  ;; Hardcoded for my input
  ;; TODO: implement a general cube folding solution
  (match [(quot x 50) (quot y 50) dir]
    [_ 0 [-1 0]] [[0 (- 149 y)]   [1  0]]
    [_ 1 [-1 0]] [[(- y 50) 100]  [0  1]]
    [_ 2 [-1 0]] [[50 (- 149 y)]  [1  0]]
    [_ 3 [-1 0]] [[(- y 100) 0]   [0  1]]
    [_ 0 [1  0]] [[99 (- 149 y)]  [-1 0]]
    [_ 1 [1  0]] [[(+ 50 y) 49]   [0 -1]]
    [_ 2 [1  0]] [[149 (- 149 y)] [-1 0]]
    [_ 3 [1  0]] [[(- y 100) 149] [0 -1]]
    [0 _ [0 -1]] [[50 (+ 50 x)]   [1  0]]
    [1 _ [0 -1]] [[0 (+ 100 x)]   [1  0]]
    [2 _ [0 -1]] [[(- x 100) 199] [0 -1]]
    [0 _ [0  1]] [[(+ x 100) 0]   [0  1]]
    [1 _ [0  1]] [[49 (+ 100 x)]  [-1 0]]
    [2 _ [0  1]] [[99 (- x 50)]   [-1 0]]))

(defn- wrapped [grid wrap-fn pos dir]
  (let [pos' (p/pos+ pos dir)]
    (if-not (contains? grid pos')
      (wrap-fn grid pos' dir)
      [pos' dir])))

(defn- walk [grid wrap-fn dist pos d]
  (reduce
   (fn [[p d] _]
     (let [[p' d'] (wrapped grid wrap-fn p d)]
       (if (= \. (grid p'))
         [p' d']
         (reduced [p d]))))
   [pos d]
   (range dist)))

(defn- walk-path [start path grid wrap-fn]
  (reduce
   (fn [[pos dir] move]
     (if (number? move)
       (walk grid wrap-fn move pos dir)
       [pos (move dir)]))
   start
   path))

(defn password [input wrap-fn]
  (let [[grid path] (s/parse-blocks input [#(g/->sparse-grid % #{\. \#}) parse-path])
        start [(c/first-where #(= \. (grid %)) (for [x (range (g/width grid))] [x 0])) [1 0]]
        [[x y] dir] (walk-path start path grid wrap-fn)]
    (+ (* 4 (inc x))
       (* 1000 (inc y))
       (c/index-of dirs dir))))

(defn part1 [input] (password input wrap-torus))

(defn part2 [input] (password input wrap-cube))
