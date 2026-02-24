;; https://adventofcode.com/2023/day/5
(ns aoc2023.day05
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2023 5))

(defn- range-mapping [[dest-start source-start length]]
  [source-start
   (+ source-start length -1)
   (- dest-start source-start)])

(defn- parse-almanac [input]
  (let [[seeds & maps] (s/blocks input)]
    {:seeds (s/ints seeds)
     :maps  (->> (map s/ints maps)
                 (map #(partition 3 %))
                 (map #(map range-mapping %)))}))

(defn- apply-mappings-to-value [n mappings]
  (reduce
   (fn [n [start end offset]]
     (if (<= start n end)
       (reduced (+ n offset))
       n))
   n
   mappings))

(defn- apply-maps-to-value [maps seed]
  (reduce apply-mappings-to-value seed maps))

(defn- range-intersect
  [[a-start a-end] [b-start b-end]]
  (let [start (max a-start b-start)
        end   (min a-end b-end)]
    (if (< end start)
      [nil [[a-start a-end]]]
      (let [left  (when (< a-start start) [a-start (dec start)])
            right (when (< end a-end) [(inc end) a-end])]
        [[start end] (filterv some? [left right])]))))

(defn- apply-mappings-to-ranges [ranges mappings]
  (loop [unmapped ranges
         [mapping & mappings] mappings
         translated []]
    (if (nil? mapping)
      (concat translated unmapped)
      (let [[start end offset] mapping
            {:keys [new-translated new-unmapped]}
            (reduce (fn [acc r]
                      (let [[inside outside] (range-intersect r [start end])]
                        (cond-> acc
                          inside (update :new-translated conj (mapv #(+ offset %) inside))
                          true   (update :new-unmapped into outside))))
                    {:new-translated [] :new-unmapped []}
                    unmapped)]
        (recur new-unmapped mappings (into translated new-translated))))))

(defn- apply-maps-to-range [maps seed-range]
  (reduce apply-mappings-to-ranges [seed-range] maps))

(defn part1 [input]
  (let [{:keys [seeds maps]} (parse-almanac input)]
    (reduce min (map #(apply-maps-to-value maps %) seeds))))

(defn part2 [input]
  (let [{:keys [seeds maps]} (parse-almanac input)
        seed-ranges (map (fn [[s l]] [s (+ s l -1)]) (partition 2 seeds))]
    (->> seed-ranges
         (mapcat #(apply-maps-to-range maps %))
         (map first)
         (reduce min))))
