;; https://adventofcode.com/2016/day/10
 (ns aoc2016.day10
   (:require
    [aoc.day :as d]
    [aoc.util.collection :as c]
    [aoc.util.string :as s]
    [clojure.string :as str]))

(defn input [] (d/day-input 2016 10))

(defn- parse-locations [line]
  (->> line
       (re-seq #"(bot|output) (\d+)")
       (map (fn [[_ type num]] [(keyword type) (s/int num)]))))

(defn- parse-factory [input]
  (reduce (fn [factory line]
            (let [locs (parse-locations line)]
              (cond
                (str/starts-with? line "value")
                (update-in factory (first locs) conj (s/int line))

                (str/starts-with? line "bot")
                (assoc-in factory [:rules (second (first locs))] (rest locs)))))
          {:rules {}}
          (s/lines input)))

(defn- apply-rule [factory [bot low high]]
  (let [chips (sort (get-in factory [:bot bot]))]
    (-> factory
        (update-in [:bot bot] empty)
        (update-in low conj (first chips))
        (update-in high conj (second chips)))))

(defn- full-bots [factory]
  (->> (:bot factory)
       (filter #(= 2 (count (val %))))
       (map key)))

(defn- step [factory]
  (let [bots (full-bots factory)]
    (reduce (fn [f bot]
              (apply-rule f (cons bot (get-in f [:rules bot]))))
            factory
            bots)))

(defn- bot-holding [chips factory]
  (some->> (:bot factory)
           (c/first-where #(= (set (val %)) chips))
           key))

(defn part1
  ([input] (part1 input #{61 17}))
  ([input chips]
   (->> (parse-factory input)
        (iterate step)
        (some (partial bot-holding chips)))))

(defn part2 [input]
  (let [targets #{0 1 2}]
    (->> (parse-factory input)
         (iterate step)
         (map :output)
         (c/first-where (fn [outputs] (and outputs (every? outputs targets))))
         (filter (comp targets key))
         vals
         flatten
         (reduce *))))
