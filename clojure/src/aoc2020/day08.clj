;; https://adventofcode.com/2020/day/8
 (ns aoc2020.day08
   (:require
    [aoc.day :as d]
    [aoc.util.string :as s]
    [clojure.string :as str]))

(defn input [] (d/day-input 2020 8))

(defn- parse-program [input]
  (->> (s/lines input)
       (map #(str/split % #" "))
       (mapv (fn [[op arg]] [(keyword op) (s/int arg)]))))

(defn- run [program]
  (loop [pc 0, acc 0, seen #{}]
    (cond
      (seen pc) {:looped acc}
      (>= pc (count program)) {:exited acc}
      :else (let [[op arg] (program pc)
                  seen' (conj seen pc)]
              (case op
                :acc (recur (inc pc)   (+ acc arg) seen')
                :jmp (recur (+ pc arg) acc         seen')
                :nop (recur (inc pc)   acc         seen'))))))

(defn- patches
  [program patch-fn]
  (keep-indexed
   (fn [i [op arg]]
     (when-let [new-op (patch-fn op)]
       (assoc program i [new-op arg])))
   program))

(defn part1 [input] (:looped (run (parse-program input))))

(defn part2 [input]
  (->> (patches (parse-program input) {:jmp :nop, :nop :jmp})
       (map run)
       (some :exited)))
