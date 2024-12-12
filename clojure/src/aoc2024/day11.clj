;; https://adventofcode.com/2024/day/11
(ns aoc2024.day11
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [clojure.math :as math]))

(def input (d/day-input 2024 11))

(def stone-count-for
  (memoize
   (fn [blinks num]
     (cond
       (zero? blinks) 1
       (zero? num)    (stone-count-for (dec blinks) 1)
       :else (let [digits (m/num-digits num)]
               (if (even? digits)
                 (let [d (int (math/pow 10 (/ digits 2)))]
                   (+ (stone-count-for (dec blinks) (quot num d))
                      (stone-count-for (dec blinks) (rem num d))))
                 (stone-count-for (dec blinks) (* 2024 num))))))))

(defn blink [input blinks]
  (transduce (map (partial stone-count-for blinks)) + (s/parse-nums input)))

(defn part1 [input] (blink input 25))

(defn part2 [input] (blink input 75))
