;; https://adventofcode.com/2023/day/12
(ns aoc2023.day12
  (:require
   [aoc.day :as d]
   [aoc.util.memoize :refer [letfn-mem]]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2023 12))

(def ^:private operational? #{\. \?})
(def ^:private damaged?     #{\# \?})

(defn- parse-row [line] [(first (str/split line #" ")) (s/ints line)])

(defn- unfold [[springs damaged]]
  [(str/join "?" (repeat 5 springs)) (apply concat (repeat 5 damaged))])

(defn- arrangements [[springs damaged]]
  (let [springs (vec springs)
        ns (count springs)
        damaged (vec damaged)
        nd (count damaged)]
    (letfn-mem
     [(search [s-idx d-idx]
              (cond
                (= d-idx nd) (if (every? operational? (subvec springs s-idx)) 1 0)
                (>= s-idx ns) 0
                :else
                (let [run (nth damaged d-idx)
                      spring (nth springs s-idx)
                      match-run #(let [end (+ s-idx run)]
                                   (if (and (<= end ns)
                                            (every? damaged? (subvec springs s-idx end))
                                            (or (= end ns) (operational? (nth springs end))))
                                     (search (min ns (inc end)) (inc d-idx))
                                     0))]
                  (cond
                    (= spring \.) (search (inc s-idx) d-idx)
                    (= spring \#) (match-run)
                    (= spring \?) (+ (match-run) (search (inc s-idx) d-idx))))))]
     (search 0 0))))

(defn- sum-arrangements [input parse-fn]
  (transduce (map (comp arrangements parse-fn)) + 0 (s/lines input)))

(defn part1 [input] (sum-arrangements input parse-row))

(defn part2 [input] (sum-arrangements input (comp unfold parse-row)))
