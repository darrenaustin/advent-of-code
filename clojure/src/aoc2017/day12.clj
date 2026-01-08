;; https://adventofcode.com/2017/day/12
   (ns aoc2017.day12
     (:require
      [aoc.day :as d]
      [aoc.util.collection :as c]
      [aoc.util.string :as s]))

(defn input [] (d/day-input 2017 12))

(defn- parse-pipes [input]
  (->> (s/lines input)
       (map #(let [[n & ps] (s/ints %)] [n (set ps)]))
       (into {})))

(defn- connected-to [n pipes]
  (loop [q (conj c/empty-queue n), connected #{n}]
    (if-let [current (peek q)]
      (let [new-connect (remove connected (pipes current))]
        (recur (into (pop q) new-connect) (apply conj connected new-connect)))
      connected)))

(defn- connected-groups [pipes]
  (loop [nodes (keys pipes), visited #{}, groups []]
    (if-let [n (first nodes)]
      (if (visited n)
        (recur (rest nodes) visited groups)
        (let [group (connected-to n pipes)]
          (recur (rest nodes) (into visited group) (conj groups group))))
      groups)))

(defn part1 [input]
  (->> (parse-pipes input)
       (connected-to 0)
       count))

(defn part2 [input]
  (->> (parse-pipes input)
       connected-groups
       count))
