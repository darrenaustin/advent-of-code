;; https://adventofcode.com/2015/day/21
(ns aoc2015.day21
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]
   [clojure.math.combinatorics :refer [combinations]]))

(defn input [] (d/day-input 2015 21))

(def ^:private shop
  {:weapons [{:cost   8 :damage 4 :armor 0}
             {:cost  10 :damage 5 :armor 0}
             {:cost  25 :damage 6 :armor 0}
             {:cost  40 :damage 7 :armor 0}
             {:cost  74 :damage 8 :armor 0}]
   :armor   [{:cost  13 :damage 0 :armor 1}
             {:cost  31 :damage 0 :armor 2}
             {:cost  53 :damage 0 :armor 3}
             {:cost  75 :damage 0 :armor 4}
             {:cost 102 :damage 0 :armor 5}]
   :rings   [{:cost  25 :damage 1 :armor 0}
             {:cost  50 :damage 2 :armor 0}
             {:cost 100 :damage 3 :armor 0}
             {:cost  20 :damage 0 :armor 1}
             {:cost  40 :damage 0 :armor 2}
             {:cost  80 :damage 0 :armor 3}]})

(defn- parse-boss [input]
  (let [[hp damage armor] (s/ints input)]
    {:hp hp :damage damage :armor armor}))

(defn- battle [boss player]
  (let [player-turns (/ (:hp boss) (max 1 (- (:damage player) (:armor boss))))
        boss-turns (/ (:hp player) (max 1 (- (:damage boss) (:armor player))))]
    (if (<= player-turns boss-turns)
      :player
      :boss)))

(defn- loadouts [hp]
  (for [weapon (:weapons shop)
        armor (concat [{}] (:armor shop))
        [ring1 ring2] (combinations (concat [{} {}] (:rings shop)) 2)]
    (assoc (merge-with + weapon armor ring1 ring2) :hp hp)))

(defn- cost-for [input cost-cmp winner]
  (let [boss (parse-boss input)]
    (->> (loadouts 100)
         (sort-by :cost cost-cmp)
         (filter #(= winner (battle boss %)))
         first
         :cost)))

(defn part1 [input] (cost-for input < :player))

(defn part2 [input] (cost-for input > :boss))
