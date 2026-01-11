;; https://adventofcode.com/2019/day/7
(ns aoc2019.day07
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc2019.intcode :as ic]
   [clojure.math.combinatorics :as combo]))

(defn input [] (d/day-input 2019 7))

(defn- run-amp [signal amp]
  (let [amp' (-> amp
                 (ic/update-io [signal] nil)
                 ic/execute)]
    [(first (:output amp')) amp']))

(defn- run-amps [signal amps]
  (c/map-accum run-amp signal amps))

(defn- init-amps [program phases]
  (let [amp (-> program ic/parse-program ic/init-machine)]
    (mapv #(second (run-amp % amp)) phases)))

(defn- thrust [signal amps]
  (first (run-amps signal amps)))

(defn- feedback-thrust [signal amps]
  (loop [[signal amps] [signal amps]]
    (if (every? #(= :halted (:status %)) amps)
      signal
      (recur (run-amps signal amps)))))

(defn- max-thrust [program phase-settings thrust-fn]
  (apply max
         (map (fn [phases]
                (->> phases
                     (init-amps program)
                     (thrust-fn 0)))
              (combo/permutations phase-settings))))

(defn part1 [input] (max-thrust input [0 1 2 3 4] thrust))

(defn part2 [input] (max-thrust input [5 6 7 8 9] feedback-thrust))
