;; https://adventofcode.com/2025/day/10
 (ns aoc2025.day10
   (:require
    [aoc.day :as d]
    [aoc.util.lp-solver :as lp]
    [aoc.util.math :as m]
    [aoc.util.string :as s]
    [clojure.string :as str])
   (:import
    [clojure.lang PersistentQueue]))

(defn input [] (d/day-input 2025 10))

(defonce machine-regex #"^\[(.*?)\]\s*((?:\([^)]+\)\s*)+)\s*\{(.*)\}$")

(defn parse-machines [input]
  (map (fn [line]
         (let [[[_ lights-str buttons-str joltage-str]] (re-seq machine-regex line)
               buttons (mapv s/parse-ints (str/split buttons-str #" "))
               joltages (s/parse-ints joltage-str)]
           {:lights lights-str, :buttons buttons :joltages joltages}))
       (str/split-lines input)))

(defn- button-mask [button mask-bits]
  (m/sum (map #(bit-shift-left 1 (- (dec mask-bits) %)) button)))

(defn min-presses-for-lights [{:keys [lights buttons] :as machine}]
  (let [goal (read-string (str "2r" (str/join (mapv #(if (= \# %) 1 0) lights))))
        button-masks (mapv #(button-mask % (count lights)) buttons)]
    (loop [open (conj PersistentQueue/EMPTY [0 0]), visited #{0}]
      (when-let [[state depth] (peek open)]
        (if (= state goal)
          depth
          (let [[open' visited']
                (reduce (fn [[o v] b]
                          (let [new-state (bit-xor state b)]
                            (if-not (v new-state)
                              [(conj o [new-state (inc depth)]) (conj v new-state)]
                              [o v])))
                        [(pop open) visited]
                        button-masks)]
            (recur open' visited')))))))

(defn min-presses-for-joltage [{:keys [buttons joltages] :as machine}]
  (let [buttons (mapv set buttons)
        objective (vec (repeat (count buttons) 1))
        constraints (map-indexed (fn [i goal]
                                   [(mapv #(if (% i) 1 0) buttons) :eq goal])
                                 joltages)
        result (lp/solve-ilp objective constraints :minimize)]
    (:value result)))

(defn part1 [input]
  (->> input
       parse-machines
       (map min-presses-for-lights)
       m/sum))

(defn part2 [input]
  (->> input
       parse-machines
       (map min-presses-for-joltage)
       m/sum))
