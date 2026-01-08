;; https://adventofcode.com/2017/day/9
(ns aoc2017.day09
  (:require
   [aoc.day :as d]))

(defn input [] (d/day-input 2017 9))

(defn- process-stream [input]
  (loop [[c & chars] input
         {:keys [depth in-garbage?] :as state} {:score 0
                                                :garbage 0
                                                :depth 0
                                                :in-garbage? false}]
    (cond
      (nil? c) state
      (= c \!) (recur (rest chars) state)
      in-garbage? (if (= c \>)
                    (recur chars (assoc state :in-garbage? false))
                    (recur chars (update state :garbage inc)))
      (= c \{) (recur chars (-> state
                                (update :depth inc)
                                (update :score + (inc depth))))
      (= c \}) (recur chars (update state :depth dec))
      (= c \<) (recur chars (assoc state :in-garbage? true))
      :else    (recur chars state))))

(defn part1 [input] (:score (process-stream input)))

(defn part2 [input] (:garbage (process-stream input)))
