;; https://adventofcode.com/2016/day/11
 (ns aoc2016.day11
   (:require
    [aoc.day :as d]
    [aoc.util.collection :as c]
    [aoc.util.string :as s]
    [clojure.data.priority-map :refer [priority-map]]
    [clojure.math.combinatorics :as combo]
    [clojure.set :refer [intersection]]))

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

(defn- distance [[elevator pairs]]
  (reduce #(+ %1 (- 3 %2)) (- 3 elevator) (flatten pairs)))

(defn- goal? [[elevator pairs]]
  (and (= elevator 3)
       (= #{[3 3]} (set pairs))))

(defn valid-pairs? [pairs]
  (let [gens  (set (keep first pairs))
        chips (set (keep second (filter #(apply not= %) pairs)))]
    (empty? (intersection gens chips))))

(defn next-steps [[floor pairs]]
  (let [positions   (vec (flatten pairs))
        lower-bound (apply min positions)
        places      (c/indexes-by #(= % floor) positions)
        moving      (concat (combo/combinations places 2) (map list places))]
    (for [move moving
          dir [1 -1]
          :let  [floor' (+ floor dir)]
          :when (<= lower-bound floor' 3)
          :let  [pairs' (->> (reduce #(assoc %1 %2 floor') positions move)
                             (partition 2)
                             (map vec)
                             sort)]
          :when (valid-pairs? pairs')]
      [floor' pairs'])))

(defn min-steps [start]
  (loop [states (priority-map [start 0] (distance start)), visited? #{}]
    (when-let [[[building steps] _] (peek states)]
      (if (goal? building)
        steps
        (let [next-states (mapcat (fn [[[building steps] _]]
                                    (->> (next-steps building)
                                         (remove visited?)
                                         (map (fn [b] [b (inc steps)]))))
                                  states)]
          (recur (into (priority-map) (map (fn [s] [s (distance (first s))]) next-states))
                 (apply conj visited? (map first next-states))))))))

(defn part1 [input] (min-steps (parse-building input)))

(defn part2 [input] (min-steps (parse-building input [[0 0] [0 0]])))
