;; https://adventofcode.com/2020/day/14
 (ns aoc2020.day14
   (:require
    [aoc.day :as d]
    [aoc.util.math :refer [sum]]
    [aoc.util.string :as s]
    [clojure.string :as str]))

(defn input [] (d/day-input 2020 14))

(defn- parse-mask [s]
  (vec (reverse (last (str/split s #" ")))))

(defn- mask-value [mask value]
  (reduce-kv
   (fn [val idx bit]
     (case bit
       \0 (bit-clear val idx)
       \1 (bit-set val idx)
       \X val))
   value
   mask))

(defn- mask-addresses [mask address]
  (reduce-kv
   (fn [values idx bit]
     (case bit
       \0 values
       \1 (map #(bit-set % idx) values)
       \X (mapcat #(vector (bit-set % idx) (bit-clear % idx)) values)))
   [address]
   mask))

(defn- mem-masked-value [state [addr value]]
  (assoc-in state [:mem addr] (mask-value (:mask state) value)))

(defn- mem-masked-address [state [addr value]]
  (reduce (fn [state address] (assoc-in state [:mem address] value))
          state
          (mask-addresses (:mask state) addr)))

(defn- apply-instr [mem-fn]
  (fn [state instr]
    (if (str/starts-with? instr "mask")
      (assoc state :mask (parse-mask instr))
      (mem-fn state (s/ints instr)))))

(defn- mem-sum [input mem-fn]
  (->> (s/lines input)
       (reduce (apply-instr mem-fn) {:mask [\X], :mem {}})
       :mem
       vals
       sum))

(defn part1 [input] (mem-sum input mem-masked-value))

(defn part2 [input] (mem-sum input mem-masked-address))
