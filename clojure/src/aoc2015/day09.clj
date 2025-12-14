;; https://adventofcode.com/2015/day/9
(ns aoc2015.day09
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [clojure.math.combinatorics :as combo]))

(defn input [] (d/day-input 2015 9))

(defn parse-route [line]
  (let [[_ from to dist] (re-find #"(.+) to (.+) = (\d+)" line)]
    [from to (s/int dist)]))

(defn parse-routes [input]
  (reduce (fn [routes line]
            (let [[from to dist] (parse-route line)]
              (-> routes
                  (assoc-in [from to] dist)
                  (assoc-in [to from] dist))))
          {} (s/lines input)))

(defn best-distance [input best-fn]
  (let [routes (parse-routes input)
        paths (combo/permutations (keys routes))]
    (apply best-fn (->> paths
                        (map (partial partition 2 1))
                        (map #(m/sum (map (partial get-in routes) %)))))))

(defn part1 [input] (best-distance input min))

(defn part2 [input] (best-distance input max))
