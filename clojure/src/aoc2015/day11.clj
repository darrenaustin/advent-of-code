;; https://adventofcode.com/2015/day/11
(ns aoc2015.day11
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2015 11))

(def valid-letters (remove #{\i \o \l} s/lower-alphabet))

(def inc-letter
  (into {} (map vec (partition 2 1 valid-letters))))

(def increasing-triplet-regex
  (->> valid-letters
       (partition 3 1)
       (map str/join)
       (str/join "|")
       re-pattern))

(def unique-doubles-regex #"(.)\1.*(.)(?!\1)\2")

(defn inc-password [password]
  (letfn [(increment [chrs]
            (if (empty? chrs)
              [\a]
              (let [[chr & more] chrs]
                (if (= \z chr)
                  (concat [\a] (increment more))
                  (concat [(inc-letter chr)] more)))))]
    (-> password reverse increment reverse str/join)))

(defn valid? [s]
  (and (re-find increasing-triplet-regex s)
       (re-find unique-doubles-regex s)))

(defn next-passwords [s]
  (filter valid? (rest (iterate inc-password s))))

(defn part1 [input] (first (next-passwords input)))

(defn part2 [input] (second (next-passwords input)))
