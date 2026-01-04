;; https://adventofcode.com/2016/day/18
(ns aoc2016.day18
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]))

(defn input [] (d/day-input 2016 18))

(defn- parse-row [s] (mapv #(if (= % \.) 1 0) s))

(defn- next-row-and-count [row]
  ;; Based on the trap rules, a tile is safe if the
  ;; upper-left and upper-right tiles are the same.
  ;;
  ;; For performance, we calculate the next row and
  ;; the sum of safe tiles for it at the same time.
  (let [n (count row)
        ;; Special case the first and last tiles:
        first-tile (if (= 1 (nth row 1)) 1 0)
        last-tile (if (= (nth row (- n 2)) 1) 1 0)]
    (loop [i 1, count first-tile, row' (transient [first-tile])]
      (if (< i (dec n))
        (let [left (nth row (dec i))
              right (nth row (inc i))
              tile (if (= left right) 1 0)]
          (recur (inc i) (+ count tile) (conj! row' tile)))
        [(persistent! (conj! row' last-tile)) (+ count last-tile)]))))

(defn- safe-tiles [input num-rows]
  (let [first-row (parse-row input)]
    (loop [sum (m/sum first-row), row first-row, rows-left (dec num-rows)]
      (if (zero? rows-left)
        sum
        (let [[next-row count] (next-row-and-count row)]
          (recur (+ sum count) next-row (dec rows-left)))))))

(defn part1
  ([input] (part1 input 40))
  ([input num-rows] (safe-tiles input num-rows)))

(defn part2 [input] (safe-tiles input 400000))
