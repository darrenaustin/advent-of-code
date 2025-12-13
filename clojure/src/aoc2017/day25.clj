;; https://adventofcode.com/2017/day/25
(ns aoc2017.day25
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2017 25))

(def dirs {"right" 1, "left" -1})

(defn parse-steps [input]
  (re-find #"(?s).*value (0|1)\..*(right|left).*state (.)\." input))

(defn parse-state [input]
  (let [[state zero-steps one-steps] (str/split input #"If")
        [_ write0 move0 next0] (parse-steps zero-steps)
        [_ write1 move1 next1] (parse-steps one-steps)]
    [(subs state 9 10)
     [{:write (s/int write0) :move (dirs move0) :next next0}
      {:write (s/int write1) :move (dirs move1) :next next1}]]))

(defn parse [input]
  (let [[init & states] (str/split input #"\n\n")]
    {:state  (subs init 15 16)
     :pos    0
     :tape   {}
     :steps  (s/int init)
     :states (into {} (map parse-state states))}))

(defn execute [{:keys [state pos tape steps states] :as machine}]
  (if (zero? steps)
    machine
    (let [instr (get-in states [state (get tape pos 0)])]
      (recur {:state  (:next instr)
              :pos    (+ pos (:move instr))
              :tape   (assoc tape pos (:write instr))
              :steps  (dec steps)
              :states states}))))

(defn part1 [input]
  (->> input
       parse
       execute
       :tape
       vals
       (c/count-where #{1})))

(defn part2 [_] "🎄 Got em all! 🎉")
