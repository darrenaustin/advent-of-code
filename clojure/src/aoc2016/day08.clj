;; https://adventofcode.com/2016/day/8
 (ns aoc2016.day08
   (:require
    [aoc.day :as d]
    [aoc.util.ascii-art :as ascii-art]
    [aoc.util.collection :as c]
    [aoc.util.grid-vec :as g]
    [aoc.util.string :as s]
    [clojure.string :as str]))

(defn input [] (d/day-input 2016 8))

(defn rect [grid [w h]]
  (g/set-sub-grid grid [0 0] (g/make-grid-vec w h true)))

(defn rotate-column [grid [col by]]
  (g/set-column grid col (c/rotate-right by (g/column grid col))))

(defn rotate-row [grid [row by]]
  (g/set-row grid row (c/rotate-right by (g/row grid row))))

(defn apply-instruction [g i]
  (cond
    (str/starts-with? i "rect") (rect g (s/ints i))
    (str/starts-with? i "rotate column") (rotate-column g (s/ints i))
    (str/starts-with? i "rotate row") (rotate-row g (s/ints i))))

(defn message [input [w h]]
  (reduce apply-instruction
          (g/make-grid-vec w h false)
          (s/lines input)))

(defn part1
  ([input] (part1 input [50 6]))
  ([input [w h]]
   (->> (message input [w h])
        vals
        (filter true?)
        count)))

(defn part2 [input]
  (-> (message input [50 6])
      (g/format-rows :value-fn {true \# false \space})
      ascii-art/ocr))
