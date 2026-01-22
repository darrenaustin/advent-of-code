;; https://adventofcode.com/2022/day/3
 (ns aoc2022.day03
   (:require
    [aoc.day :as d]
    [aoc.util.string :as s]
    [clojure.set :as set]))

(defn input [] (d/day-input 2022 3))

(def ^:private priority
  (zipmap (str s/alphabet-lower s/alphabet-upper) (range 1 53)))

(defn- common-item [colls]
  (->> (map set colls)
       (apply set/intersection)
       first))

(defn- halves-of [sack]
  (split-at (quot (count sack) 2) sack))

(defn- total-priorities [input select-fn]
  (transduce
   (map (comp priority common-item))
   +
   0
   (select-fn (s/lines input))))

(defn part1 [input] (total-priorities input (partial map halves-of)))

(defn part2 [input] (total-priorities input (partial partition 3)))
