;; https://adventofcode.com/2024/day/23
(ns aoc2024.day23
  (:require [aoc.day :as d]
            [aoc.util.string :as s]
            [clojure.set :as set]
            [clojure.string :as str]))

(defn input [] (d/day-input 2024 23))

(defn parse-edges [input]
  (map #(str/split % #"-") (str/split-lines input)))

(defn parse-graph [input]
  (reduce (fn [graph [v1 v2]]
            (assoc graph
              v1 (conj (get graph v1 #{}) v2)
              v2 (conj (get graph v2 #{}) v1)))
          {}
          (parse-edges input)))

(defn mutual? [graph n node-set]
  (or (node-set n) (empty? (set/difference node-set (graph n)))))

(defn clique-for [graph v]
  (loop [clique #{v} check (graph v)]
    (if (empty? check)
      clique
      (let [c (first check)]
        (if (mutual? graph c clique)
          (recur (conj clique c) (rest check))
          (recur clique (rest check)))))))

(defn cliques [graph]
  (set (mapv #(clique-for graph %) (keys graph))))

(defn connected-triples [graph including]
  (let [possible (filter (fn [i] (<= 2 (count (graph i)))) (keys graph))]
    (for [a possible
          b possible :when (s/string< a b)
          c possible :when (s/string< a b c)
          :let [triplet [a b c]]
          :when (and (some including triplet)
                         ((graph a) b)
                         ((graph a) c)
                         ((graph b) c))]
          triplet)))

(defn part1 [input]
  (let [graph   (parse-graph input)
        t-nodes (filter #(str/starts-with? % "t") (keys graph))]
    (count (connected-triples graph (set t-nodes)))))

(defn part2 [input]
  (->> input
       parse-graph
       cliques
       (apply max-key count)
       sort
       (str/join ",")))
