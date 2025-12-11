;; https://adventofcode.com/2025/day/10
 (ns aoc2025.day10
   (:require
    [aoc.day :as d]
    [aoc.util.math :as m]
    [aoc.util.string :as s]
    [clojure.math :refer [pow]]
    [clojure.string :as str])
   (:import
    [clojure.lang PersistentQueue]))

(defn input [] (d/day-input 2025 10))

(defn parse-machines [input]
  (map (fn [line]
         (let [[[_ lights-str buttons-str _]] (re-seq #"^\[(.*?)\]\s*((?:\([^)]+\)\s*)+)\s*\{(.*)\}$" line)
               lights (read-string (str "2r" (str/join (mapv #(if (= \# %) 1 0) lights-str))))
               buttons (mapv (fn [b] (m/sum (map #(int (pow 2 (- (dec (count lights-str)) %))) (s/parse-ints b))))
                             (str/split buttons-str #" "))]
           {:lights-goal lights, :buttons buttons})) (str/split-lines input)))


(defn min-presses [{:keys [lights-goal buttons] :as machine}]
  (loop [open (conj PersistentQueue/EMPTY [0 0]), visited #{0}]
    (when-let [[state depth] (peek open)]
      (if (= state lights-goal)
        depth
        (let [[open' visited']
              (reduce (fn [[o v] b] (let [new-state (bit-xor state b)]
                                      (if-not (v new-state)
                                        [(conj o [new-state (inc depth)]) (conj v new-state)]
                                        [o v])))
                      [(pop open) visited]
                      buttons)]
          (recur open' visited'))))))

(defn part1 [input]
  (->> input
       parse-machines
       (map min-presses)
       m/sum))

(defn parse-machines2 [input]
  (map (fn [line]
         (let [[[_ _ buttons-str joltage-str]] (re-seq #"^\[(.*?)\]\s*((?:\([^)]+\)\s*)+)\s*\{(.*)\}$" line)
               joltage-goal (s/parse-ints joltage-str)
               buttons (mapv s/parse-ints (str/split buttons-str #" "))]
           {:joltage-goal joltage-goal, :buttons buttons}))
       (str/split-lines input)))


(defn press-button [joltages button]
  (reduce (fn [js c] (update js c inc)) joltages button))

(defn under-joltage? [goal joltage]
  (every? #(<= (nth joltage %) (nth goal %)) (range (count goal))))

(defn min-presses2 [{:keys [joltage-goal buttons] :as machine}]
  (let [starting (vec (repeat (count joltage-goal) 0))]
    (loop [open (conj PersistentQueue/EMPTY [starting 0]), visited #{starting}]
      (when-let [[state depth] (peek open)]
        (if (= state joltage-goal)
          depth
          (let [[open' visited']
                (reduce (fn [[o v] b] (let [new-state (press-button state b)]
                                        (if (and (not (v new-state)) (under-joltage? joltage-goal new-state))
                                          [(conj o [new-state (inc depth)]) (conj v new-state)]
                                          [o v])))
                        [(pop open) visited]
                        buttons)]
            (recur open' visited')))))))

;; TODO: this doesn't work for the real input as it is way too slow. Need
;; to look into some kind of linear solver or something.

;; TODO: also need to cleanup the code duplication above
(defn part2 [input]
  (->> input
       parse-machines2
       (map min-presses2)
       m/sum))
