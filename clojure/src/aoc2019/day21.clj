;; https://adventofcode.com/2019/day/21
(ns aoc2019.day21
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]
   [aoc2019.intcode :as ic]
   [clojure.string :as str]))

(defn input [] (d/day-input 2019 21))

(defn- springscript [lines]
  (->> (concat lines [""])
       (str/join "\n")
       s/str->ascii))

(def short-range-script
  (springscript
   ["OR A T"
    "AND B T"
    "AND C T"
    "NOT T T"
    "AND D T"
    "OR T J"
    "WALK"]))

(def long-range-script
  (springscript
   ["NOT H T"
    "OR C T"
    "AND A T"
    "AND B T"
    "NOT T T"
    "AND D T"
    "OR T J"
    "RUN"]))

(defn- run-droid [input script]
  (->> (ic/run (ic/parse-program input) script nil)
       :output
       (remove s/ascii?)
       first))

(defn part1 [input] (run-droid input short-range-script))

(defn part2 [input] (run-droid input long-range-script))
