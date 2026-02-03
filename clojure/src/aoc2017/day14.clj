;; https://adventofcode.com/2017/day/14
(ns aoc2017.day14
  (:require
   [aoc.day :as d]
   [aoc.util.collection :refer [count-where]]
   [aoc.util.grid :as g]
   [aoc.util.pos :as p]
   [aoc2017.knot-hash :refer [knot-hash]]
   [clojure.set :as set]
   [clojure.string :as str]))

(defn input [] (d/day-input 2017 14))

(def hex-map
  {\0 "0000" \1 "0001" \2 "0010" \3 "0011"
   \4 "0100" \5 "0101" \6 "0110" \7 "0111"
   \8 "1000" \9 "1001" \a "1010" \b "1011"
   \c "1100" \d "1101" \e "1110" \f "1111"})

(defn binary [hex]
  (str/join (map hex-map hex)))

(defn knot-hashs [input]
  (mapv #(binary (knot-hash (str input "-" %))) (range 128)))

(defn connected-components [nodes neighbors-fn]
  (loop [nodes (set nodes)
         components []]
    (if (empty? nodes)
      components
      (let [root (first nodes)
            ;; BFS to find the component
            component (loop [q [root] visited #{root}]
                        (if (empty? q)
                          visited
                          (let [n (peek q)
                                nbrs (filter nodes (neighbors-fn n))
                                new-nbrs (remove visited nbrs)]
                            (recur (into (pop q) new-nbrs)
                                   (into visited new-nbrs)))))]
        (recur (set/difference nodes component)
               (conj components component))))))

(defn hash-grid [input]
  (g/->sparse-grid (knot-hashs input) #(when (= \1 %) true)))

(defn part1 [input]
  (reduce (fn [s row] (+ s (count-where #{\1} row)))
          0
          (knot-hashs input)))

(defn part2 [input]
  (let [grid (hash-grid input)]
    (count (connected-components (keys grid) p/orthogonal-to))))
