;; https://adventofcode.com/2018/day/3
 (ns aoc2018.day03
   (:require
    [aoc.day :as d]
    [aoc.util.string :as s]))

(defn input [] (d/day-input 2018 3))

(defn- parse-claim [line]
  (let [[id x y width height] (s/ints line)]
    {:id id
     :squares (for [col (range width), row (range height)]
                [(+ x col) (+ y row)])}))

(defn- parse-claims [input]
  (map parse-claim (s/lines input)))

(defn- claim-density [claims]
  (frequencies (mapcat :squares claims)))

(defn- non-overlapping? [density claim]
  (every? #(= 1 (density %)) (:squares claim)))

(defn part1 [input]
  (->> (parse-claims input)
       claim-density
       vals
       (filter #(> % 1))
       count))

(defn part2 [input]
  (let [claims (parse-claims input)
        density (claim-density claims)]
    (:id
     (first
      (filter #(non-overlapping? density %) claims)))))
