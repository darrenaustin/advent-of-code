;; https://adventofcode.com/2020/day/17
(ns aoc2020.day17
  (:require
   [aoc.day :as d]
   [aoc.util.pos :as p]
   [aoc.util.string :as s]
   [clojure.math.combinatorics :as combo]))

(defn input [] (d/day-input 2020 17))

(defn- parse-active [input dims]
  (let [rows (s/lines input)]
    (set (for [y (range (count rows)) :let [col (nth rows y)]
               x (range (count (first rows))) :when (= \# (nth col x))]
           (vec (concat [x y] (repeat (- dims 2) 0)))))))

(defn- neighbor-offsets [dims]
  (disj
   (set (apply combo/cartesian-product (repeat dims [-1 0 1])))
   (repeat dims 0)))

(defn- neighbors [pos offsets]
  (map #(p/pos+ pos %) offsets))

(defn- step-cubes [active-cubes offsets]
  (set
   (for [[cube n] (frequencies (mapcat #(neighbors % offsets) active-cubes))
         :when (if (active-cubes cube)
                 (#{2 3} n)
                 (#{3} n))]
     cube)))

(defn- boot-cubes [input dims]
  (let [active-cubes (parse-active input dims)
        offsets (neighbor-offsets dims)]
    (-> (iterate #(step-cubes % offsets) active-cubes)
        (nth 6)
        count)))

(defn part1 [input] (boot-cubes input 3))

(defn part2 [input] (boot-cubes input 4))
