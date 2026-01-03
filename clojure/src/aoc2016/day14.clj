;; https://adventofcode.com/2016/day/14
(ns aoc2016.day14
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [clojure.string :as str]
   [pandect.algo.md5 :refer [md5]]))

(defn input [] (d/day-input 2016 14))

(defn- generate-hashes [hash-fn salt]
  (map (fn [i] [i (hash-fn salt i)]) (range)))

(defn- simple-hash [salt i]
  (md5 (str salt i)))

(defn- stretched-hash [salt i]
  (c/nth-iteration md5 (str salt i) 2017))

(defn- key? [[[_ candidate] & lookahead]]
  (when-let [[_ c] (re-find #"(.)\1\1" candidate)]
    (let [quint (str/join (repeat 5 c))]
      (some #(str/includes? (second %) quint) lookahead))))

(defn- key-64-index [hash-fn input]
  (->> (generate-hashes hash-fn input)
       (partition 1001 1)
       (filter key?)
       (map ffirst)
       (drop 63)
       first))

(defn part1 [input] (key-64-index simple-hash input))

(defn part2 [input] (key-64-index stretched-hash input))
