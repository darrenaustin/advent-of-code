;; https://adventofcode.com/2016/day/11
 (ns aoc2016.day11
   (:require
    [aoc.day :as d]
    [aoc.util.collection :as c]
    [aoc.util.pathfinding :as path]
    [aoc.util.string :as s]
    [clojure.math.combinatorics :as combo]))

(defn input [] (d/day-input 2016 11))

(defn- parse-parts [line floor regex]
  (into {} (map vector
                (map second (re-seq regex line))
                (repeat floor))))

(defn- parse-floor [[floor line]]
  {:chips (parse-parts line floor #"a (\w+)-compatible microchip")
   :rtgs (parse-parts line floor #"a (\w+) generator")})

(defn- element-pair [parts element]
  [(get-in parts [:rtgs element]) (get-in parts [:chips element])])

(defn- parse-building
  ([input] (parse-building input []))
  ([input initial-pairs]
   (let [parts (->> (s/lines input)
                    c/indexed
                    (map parse-floor)
                    (apply merge-with merge))
         elements (keys (:chips parts))
         pairs (->> elements
                    (map (partial element-pair parts))
                    (concat initial-pairs)
                    sort
                    vec)]
     [0 pairs])))

(defn- distance [[_ pairs]]
  (let [sum (reduce (fn [s [g c]] (+ s (- 3 g) (- 3 c))) 0 pairs)]
    (/ sum 2)))

(defn- goal? [[elevator pairs]]
  (and (= elevator 3)
       (every? #(= [3 3] %) pairs)))

(defn- valid-pairs? [pairs]
  (loop [ps pairs, gen-mask 0, chip-mask 0]
    (if-let [[g c] (first ps)]
      (recur (rest ps)
             (bit-set gen-mask g)
             (if (not= g c) (bit-set chip-mask c) chip-mask))
      (zero? (bit-and gen-mask chip-mask)))))

(defn- floor-items [floor pairs]
  (->> pairs
       (keep-indexed (fn [idx [g c]]
                       (cond-> []
                         (= g floor) (conj [idx 0])
                         (= c floor) (conj [idx 1]))))
       (apply concat)))

(defn- move-items [floor pairs move]
  (reduce (fn [ps [p-idx type]]
            (update-in ps [p-idx type] (constantly floor)))
          pairs
          move))

(defn next-steps [[floor pairs]]
  (let [items (floor-items floor pairs)
        moves (concat (combo/combinations items 2) (map list items))
        min-floor (reduce (fn [m [g c]] (min m g c)) 4 pairs)]
    (for [move moves
          dir [1 -1]
          :let [floor' (+ floor dir)]
          :when (<= min-floor floor' 3)
          :let [pairs' (move-items floor' pairs move)]
          :when (valid-pairs? pairs')
          :let [pairs' (vec (sort pairs'))]]
      [floor' pairs'])))

(defn min-steps [start]
  (path/a-star-cost start
                    next-steps
                    goal?
                    :heuristic distance))

(defn part1 [input] (min-steps (parse-building input)))

(defn part2 [input] (min-steps (parse-building input [[0 0] [0 0]])))
