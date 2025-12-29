;; https://adventofcode.com/2016/day/10
 (ns aoc2016.day10
   (:require
    [aoc.day :as d]
    [aoc.util.collection :as c]
    [aoc.util.string :as s]
    [clojure.string :as str]))

(defn input [] (d/day-input 2016 10))

(defn- parse-locations [line]
  (->> (re-seq #"((bot|output) (\d+))" line)
       (map (partial take-last 2))
       (map (fn [[name num]] [(keyword name) (s/int num)]))))

(defn- parse-factory [input]
  (reduce (fn [factory line]
            (let [locs (parse-locations line)]
              (cond
                (str/starts-with? line "value")
                (update-in factory (first locs) conj (s/int line))

                (str/starts-with? line "bot")
                (assoc-in factory [:rules (second (first locs))] locs))))
          {:rules {}}
          (s/lines input)))

(defn- apply-rule [factory [from low high]]
  (let [chips (get-in factory from)]
    (-> factory
        (c/dissoc-in from)
        (update-in low conj (apply min chips))
        (update-in high conj (apply max chips)))))

(defn- full-bots [factory]
  (->> (:bot factory)
       (filter #(= 2 (count (val %))))
       (map first)))

(defn- step [factory]
  (let [rules (map #(get-in factory [:rules %]) (full-bots factory))]
    (reduce apply-rule factory rules)))

(defn- bot-holding [chips factory]
  (->> (:bot factory)
       (filter #(= (set (val %)) (set chips)))
       ffirst))

(defn part1
  ([input] (part1 input #{61 17}))
  ([input chips]
   (->> (parse-factory input)
        (iterate step)
        (keep (partial bot-holding chips))
        first)))

(defn part2 [input]
  (let [outputs #{0 1 2}]
    (->> (parse-factory input)
         (iterate step)
         (map :output)
         (drop-while #(or (empty? %) (not-every? % outputs)))
         first
         (filter #(contains? outputs (key %)))
         (map (comp first val))
         (reduce *))))
