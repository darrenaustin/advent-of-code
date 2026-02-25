;; https://adventofcode.com/2023/day/7
(ns aoc2023.day07
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2023 7))

(def ^:private card-value (zipmap "23456789TJQKA" (range 2 15)))

(def ^:private hand-value
  {'(5)         7   ; 5 of a kind
   '(4 1)       6   ; 4 of a kind
   '(3 2)       5   ; full house
   '(3 1 1)     4   ; 3 of a kind
   '(2 2 1)     3   ; two pair
   '(2 1 1 1)   2   ; pair
   '(1 1 1 1 1) 1}) ; high card

(defn- hand-rank [cards wild?]
  (let [non-wild   (remove wild? cards)
        wild       (- (count cards) (count non-wild))
        freqs      (sort > (vals (frequencies non-wild)))
        hand-freqs (cons (+ wild (or (first freqs) 0)) (rest freqs))]
    (hand-value hand-freqs)))

(defn- parse-hand [line wild?]
  (let [[cards bid] (str/split line #" ")]
    {:cards (mapv #(if (wild? %) 1 (card-value %)) cards)
     :bid   (s/int bid)
     :rank  (hand-rank cards wild?)}))

(defn- winnings [input wild?]
  (->> (s/lines input)
       (map #(parse-hand % wild?))
       (sort-by (juxt :rank :cards))
       (map-indexed (fn [i h] (* (inc i) (:bid h))))
       (reduce +)))

(defn part1 [input] (winnings input #{}))

(defn part2 [input] (winnings input #{\J}))
