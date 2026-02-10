;; https://adventofcode.com/2022/day/20
 (ns aoc2022.day20
   (:require
    [aoc.day :as d]
    [aoc.util.collection :refer [index-of]]
    [aoc.util.linked-list :as ll]
    [aoc.util.math :refer [sum]]
    [aoc.util.string :as s]))

(defn input [] (d/day-input 2022 20))

(defn- mix! [numbers]
  (let [length (dec (count numbers))
        half-len (quot length 2)]
    (doseq [node numbers]
      (let [n (mod (ll/value node) length)]
        (if (> n half-len)
          (ll/shift-left! node (- length n))
          (ll/shift-right! node n)))))
  numbers)

(defn coords [start-node]
  (let [numbers (map ll/value (iterate ll/next start-node))]
    (sum (map #(nth numbers %) [1000 2000 3000]))))

(defn- decode
  ([input] (decode input 1 1))
  ([input decrypt-key num-mixes]
   (let [numbers   (mapv #(* decrypt-key %) (s/ints input))
         start-idx (index-of numbers 0)
         nodes     (ll/make-circular-list numbers)]
     (dotimes [_ num-mixes] (mix! nodes))
     (coords (nth nodes start-idx)))))

(defn part1 [input] (decode input))

(defn part2 [input] (decode input 811589153 10))
