;; https://adventofcode.com/2017/day/13
(ns aoc2017.day13
  (:require [aoc.day :as d]
            [aoc.util.math :as m]
            [aoc.util.string :as s]
            [clojure.string :as str]))

(defn input [] (d/day-input 2017 13))

(defn parse [input]
  (mapv s/parse-ints (str/split-lines input)))

(defn caught? [time [depth range]]
  ; a scanner reaches the top (0) at multiples of 2*range - 1
  (zero? (mod (+ depth time) (* 2 (dec range)))))

(defn severity [time firewall]
  (->> firewall
       (filter #(caught? time %))
       (map m/product)
       m/sum))

(defn safe? [time firewall]
  (every? #(not (caught? time %)) firewall))

(defn safe-delay [firewall]
  (first (filter #(safe? % firewall) (range))))

(defn part1 [input] (severity 0 (parse input)))

(defn part2 [input] (safe-delay (parse input)))
