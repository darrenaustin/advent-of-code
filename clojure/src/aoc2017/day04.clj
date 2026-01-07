;; https://adventofcode.com/2017/day/4
 (ns aoc2017.day04
   (:require
    [aoc.day :as d]
    [aoc.util.string :as s]
    [clojure.string :as str]))

(defn input [] (d/day-input 2017 4))

(defn- parse-passphrases [input]
  (map #(str/split % #"\s+") (s/lines input)))

(defn- no-dups? [words]
  (= (count words) (count (set words))))

(defn- no-anagrams? [words]
  (= (count words) (count (set (map sort words)))))

(defn- num-valid [input policy]
  (count (filter policy (parse-passphrases input))))

(defn part1 [input] (num-valid input no-dups?))

(defn part2 [input] (num-valid input no-anagrams?))
