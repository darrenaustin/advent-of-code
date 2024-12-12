;; https://adventofcode.com/2015/day/2
(ns aoc2015.day02
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(def input (d/day-input 2015 2))

(defn parse-boxes [input]
  (->> input
       str/split-lines
       (map s/parse-nums)))

(defn paper-for [[l w h]]
  (let [[s1 s2 s3] [(* l w) (* l h) (* w h)]]
    (+ (* 2 (+ s1 s2 s3))
       (min s1 s2 s3))))

(defn perimeter [w h]
  (* 2 (+ w h)))

(defn ribbon-for [[l w h]]
  (+ (min (perimeter l w) (perimeter l h) (perimeter w h))
     (* l w h)))

(defn part1 [input]
  (m/sum (map paper-for (parse-boxes input))))

(defn part2 [input]
  (m/sum (map ribbon-for (parse-boxes input))))
