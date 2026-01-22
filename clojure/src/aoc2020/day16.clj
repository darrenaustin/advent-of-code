;; https://adventofcode.com/2020/day/16
(ns aoc2020.day16
  (:require
   [aoc.day :as d]
   [aoc.util.math :refer [sum]]
   [aoc.util.matrix :refer [transpose]]
   [aoc.util.string :as s]
   [clojure.set :as set]
   [clojure.string :as str]))

(defn input [] (d/day-input 2020 16))

(defn- parse-field [line]
  (let [[name ranges] (str/split line #": ")]
    [name
     (->> (s/pos-ints ranges)
          (partition 2)
          (mapcat #(range (first %) (inc (second %))))
          set)]))

(defn- parse-notes [input]
  (s/parse-blocks-map
   input
   [[:fields #(into {} (map parse-field) (s/lines %))]
    [:my-ticket s/ints]
    [:tickets #(map s/ints (rest (s/lines %)))]]))

(defn- valid-field-values [fields]
  (apply set/union (vals fields)))

(defn- valid-fields-for [fields values]
  (set (keep (fn [[name valid?]]
               (when (every? valid? values) name))
             fields)))

(defn- valid-ticket? [valid-values ticket]
  (every? valid-values ticket))

(defn- solve-constraints [col-candidates]
  (loop [candidates (sort-by (comp count second) col-candidates)
         solved     {}
         used       #{}]
    (if-let [[col options] (first candidates)]
      (let [remaining (set/difference options used)]
        (when (= 1 (count remaining))
          (let [field (first remaining)]
            (recur (rest candidates)
                   (assoc solved col field)
                   (conj used field)))))
      solved)))

(defn- solve-field-order [fields tickets]
  (->> (transpose tickets)
       (map-indexed (fn [i col] [i (valid-fields-for fields col)]))
       solve-constraints))

(defn part1 [input]
  (let [{:keys [fields tickets]} (parse-notes input)
        valid-values (valid-field-values fields)]
    (sum (remove valid-values (apply concat tickets)))))

(defn part2 [input]
  (let [{:keys [fields my-ticket tickets]} (parse-notes input)
        valid-values  (valid-field-values fields)
        valid-tickets (filter (partial valid-ticket? valid-values) tickets)
        field-order   (solve-field-order fields (conj valid-tickets my-ticket))]
    (transduce
     (keep (fn [[i name]]
             (when (str/starts-with? name "departure")
               (get my-ticket i))))
     *
     field-order)))
