;; https://adventofcode.com/2022/day/5
(ns aoc2022.day05
  (:require
   [aoc.day :as d]
   [aoc.util.matrix :as mat]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2022 5 :trim? false))

(defn- parse-stacks [block]
  (->> (s/lines block)
       butlast
       mat/transpose
       rest
       (take-nth 4)
       (mapv #(remove #{\space} %))))

(defn- parse-moves [block]
  (partition 3 (s/ints block)))

(defn- move [xform stacks [num from to]]
  (let [items (xform (take num (nth stacks (dec from))))]
    (-> stacks
        (update (dec from) (partial drop num))
        (update (dec to) (partial concat items)))))

(defn- top-stacks [input xform]
  (let [[stacks moves] (s/parse-blocks input [parse-stacks parse-moves])]
    (->> (reduce (partial move xform) stacks moves)
         (map first)
         str/join)))

(defn part1 [input] (top-stacks input reverse))

(defn part2 [input] (top-stacks input identity))
