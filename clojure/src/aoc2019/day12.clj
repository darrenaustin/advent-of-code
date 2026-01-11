;; https://adventofcode.com/2019/day/12
  (ns aoc2019.day12
    (:require
     [aoc.day :as d]
     [aoc.util.collection :as c]
     [aoc.util.math :as m]
     [aoc.util.string :as s]))

(defn input [] (d/day-input 2019 12))

(defn- parse-moons [input]
  (map #(mapv vector (s/ints %) [0 0 0]) (s/lines input)))

(defn- extract-dim [moons dim]
  (mapv #(nth % dim) moons))

(defn- calc-gravity-1d [pos moons-1d]
  (reduce (fn [dv [op _]] (+ dv (compare op pos))) 0 moons-1d))

(defn- time-step-1d [moons-1d]
  (mapv (fn [[p v]]
          (let [dv (calc-gravity-1d p moons-1d)
                nv (+ v dv)]
            [(+ p nv) nv]))
        moons-1d))

(defn- energy [moon]
  (let [potential (transduce (map (comp abs first)) + 0 moon)
        kinetic (transduce (map (comp abs second)) + 0 moon)]
    (* potential kinetic)))

(defn- system-energy [moon-dims]
  (transduce (map energy) + 0 (apply map vector moon-dims)))

(defn system-energy-for [input steps]
  (let [system (parse-moons input)
        moon-dims (mapv #(c/nth-iteration time-step-1d (extract-dim system %) steps)
                        [0 1 2])]
    (system-energy moon-dims)))

(defn- find-period [coll]
  (let [start (first coll)]
    (inc (count (take-while #(not= % start) (rest coll))))))

(defn part1 [input] (system-energy-for input 1000))

(defn part2 [input]
  (let [system (parse-moons input)
        x-cycle (find-period (iterate time-step-1d (extract-dim system 0)))
        y-cycle (find-period (iterate time-step-1d (extract-dim system 1)))
        z-cycle (find-period (iterate time-step-1d (extract-dim system 2)))]
    (m/lcm x-cycle y-cycle z-cycle)))
