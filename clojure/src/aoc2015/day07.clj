;; https://adventofcode.com/2015/day/7
 (ns aoc2015.day07
   (:require
    [aoc.day :as d]
    [aoc.util.string :as s]
    [clojure.string :as str]))

(defn input [] (d/day-input 2015 7))

(def binary-op
  {"AND" bit-and
   "OR" bit-or
   "LSHIFT" bit-shift-left
   "RSHIFT" bit-shift-right})

(defn parse-wire [line]
  (let [[input output] (str/split line #" -> ")
        inputs (str/split input #" ")]
    (case (count inputs)
      1 [output [identity inputs]]
      2 [output [bit-not (rest inputs)]]
      3 (let [[p1 op p2] inputs]
          [output [(binary-op op) [p1 p2]]]))))

(defn parse-circuit [input]
  (into {} (map parse-wire (s/lines input))))

(defn simulate [circuit]
  (let [wires (atom {})]
    (fn eval [wire]
      (if-let [constant (s/int wire)]
        constant
        (if-let [value (@wires wire)]
          value
          (let [[op params] (circuit wire)
                value (apply op (map eval params))
                result (bit-and 0xffff value)]
            (swap! wires assoc wire result)
            result))))))

(defn part1 [input]
  ((simulate (parse-circuit input)) "a"))

(defn part2 [input]
  (let [circuit (parse-circuit input)
        a ((simulate circuit) "a")
        circuit' (assoc circuit "b" [identity [(str a)]])]
    ((simulate circuit') "a")))
