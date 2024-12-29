;; https://adventofcode.com/2017/day/18
(ns aoc2017.day18
  (:require [aoc.day :as d]
            [aoc.util.string :as s]
            [clojure.string :as str]))

(defn input [] (d/day-input 2017 18))

(defn parse [input] (vec (str/split-lines input)))

(defn next-pc [process]
  (update process :pc inc))

(defn arg-val [process arg]
  (if (re-find #"[a-z]" arg) (get (:reg process) arg 0) (s/parse-int arg)))

(defn op-set [process arg1 arg2]
  (-> process
      (assoc-in [:reg arg1] (arg-val process arg2))
      next-pc))

(defn op-fn [f process arg1 arg2]
  (-> process
      (assoc-in [:reg arg1] (f (arg-val process arg1) (arg-val process arg2)))
      next-pc))

(defn op-jump [process arg1 arg2]
  (if (pos? (arg-val process arg1))
    (update process :pc + (arg-val process arg2))
    (next-pc process)))

(defn op-sound [process arg _]
  (-> process
      (assoc :sound (arg-val process arg))
      next-pc))

(defn op-recover [process _ _]
  (assoc process :status :complete))

(defn op-send [process arg _]
  (-> process
      (update :output conj (arg-val process arg))
      (update :send-count inc)
      next-pc))

(defn op-recv [process arg _]
  (if (empty? (:input process))
    (assoc process :status :waiting)
    (-> process
        (assoc-in [:reg arg] (first (:input process)))
        (assoc :input (subvec (:input process) 1)
               :status :ready)
        next-pc)))

(defn clear-output [process]
  (assoc process :output []))

(defn add-input [process input]
  (-> process
      (update :input (partial apply conj) input)
      (assoc :status :ready)))

(defn process [initial-regs ops extras]
  (merge {:reg initial-regs :pc 0 :status :ready
          :ops (merge {"snd" op-sound
                       "set" op-set
                       "add" (partial op-fn +)
                       "mul" (partial op-fn *)
                       "mod" (partial op-fn mod)
                       "rcv" op-recover
                       "jgz" op-jump}
                      ops)}
         extras))

(defn execute [process commands]
  (loop [process process]
    (cond
      (not= (:status process) :ready) process
      (>= (:pc process) (count commands)) (assoc process :status :complete)
      :else (let [[op arg1 arg2] (str/split (nth commands (:pc process)) #" ")]
              (recur ((get (:ops process) op) process arg1 arg2))))))

(defn process-co-op [id]
  (process {"p" id}
           {"snd" op-send "rcv" op-recv}
           {:output [] :input [] :send-count 0}))

(defn part1 [input]
  (:sound (execute (process {} {} {}) (parse input))))

(defn part2 [input]
  (let [commands (parse input)]
    (loop [p0 (process-co-op 0) p1 (process-co-op 1)]
      (cond
        (seq (:output p0)) (recur (clear-output p0) (add-input p1 (:output p0)))
        (seq (:output p1)) (recur (add-input p0 (:output p1)) (clear-output p1))
        (or (= :waiting (:status p0) (:status p1))
            (= :complete (:status p0) (:status p1))) (:send-count p1)
        :else (recur (execute p0 commands) (execute p1 commands))))))
