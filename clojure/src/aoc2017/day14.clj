;; https://adventofcode.com/2017/day/14
(ns aoc2017.day14
  (:require [aoc.day :as d]
            [aoc.util.collection :refer [count-where]]
            [aoc.util.grid :refer :all]
            [aoc.util.vec :as v]
            [aoc2017.knot-hash :refer [knot-hash]]
            [clojure.pprint :refer [cl-format]]
            [clojure.string :as str]))

(defn input [] (d/day-input 2017 14))

(defn binary [hex]
  (str/join (map #(cl-format nil "~16,'0b"
                             (read-string (str "16r" (str/join %))))
                 (partition 4 hex))))

(defn knot-hashs [input]
  (map #(binary (knot-hash (str input "-" %))) (range 128)))

(defn region-around [loc adjacent-fn]
  (loop [[l & ls] #{loc} region #{}]
    (if (nil? l)
      region
      (let [neighbors (remove region (adjacent-fn l))]
        (recur (set (concat ls neighbors)) (conj region l))))))

(defn flood-fill [seeds adjacent-fn]
  (loop [[s & seeds] (set seeds) regions []]
    (if (nil? s)
      regions
      (let [region (region-around s adjacent-fn)]
        (recur (apply disj (set seeds) region) (conj regions region))))))

(defn neighbors-fn [grid]
  (fn [loc]
    (filter #(= \1 (grid %)) (v/orthogonal-from loc))))

(defn hash-grid [input]
  (parse-grid (str/join "\n" (knot-hashs input))))

(defn part1 [input]
  (reduce (fn [s row] (+ s (count-where #{\1} row)))
          0
          (knot-hashs input)))

(defn part2 [input]
  (let [grid (hash-grid input)]
    (count (flood-fill (locs-where grid #{\1}) (neighbors-fn grid)))))
