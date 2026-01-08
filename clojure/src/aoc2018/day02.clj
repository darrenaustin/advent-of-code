;; https://adventofcode.com/2018/day/2
(ns aoc2018.day02
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2018 2))

(defn- checksum [input]
  (let [freqs (map (comp set vals frequencies) (s/lines input))]
    (* (count (filter #(contains? % 2) freqs))
       (count (filter #(contains? % 3) freqs)))))

(defn- common-chars [s1 s2]
  (str/join (map (fn [c1 c2] (when (= c1 c2) c1)) s1 s2)))

(defn- one-char-off [[s1 s2]]
  (let [common (common-chars s1 s2)]
    (when (= (count common) (dec (count s1)))
      common)))

(defn part1 [input] (checksum input))

(defn part2 [input]
  (->> (s/lines input)
       c/pairs
       (keep one-char-off)
       first))
