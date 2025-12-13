;; https://adventofcode.com/2015/day/6
(ns aoc2015.day06
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2015 6))

(defn parse-commands [input]
  (map #(let [[_ operation range] (re-find #"([a-z ]+)( .*)" %)]
          [operation (s/ints range)])
       (s/lines input)))

(defn light-command1! [lights command idx]
  (case command
    "turn on"  (assoc! lights idx 1)
    "toggle"   (assoc! lights idx (if (= (lights idx) 1) 0 1))
    "turn off" (assoc! lights idx 0)))

(defn light-command2! [lights command idx]
  (let [val (lights idx)]
    (case command
      "turn on"  (assoc! lights idx (inc val))
      "toggle"   (assoc! lights idx (+ val 2))
      "turn off" (assoc! lights idx (max 0 (dec val))))))

(defn update-lights! [command-fn! lights [command [x1 y1 x2 y2]]]
  (doseq [x (range x1 (inc x2))
          y (range y1 (inc y2))]
    (let [idx (+ (* y 1000) x)]
      (command-fn! lights command idx)))
  lights)

(defn solve [input command-fn!]
  (let [commands (parse-commands input)
        lights (vec (repeat (* 1000 1000) 0))]
    (->> (reduce (partial update-lights! command-fn!)
                 (transient lights)
                 commands)
         persistent!
         m/sum)))

(defn part1 [input] (solve input light-command1!))

(defn part2 [input] (solve input light-command2!))
