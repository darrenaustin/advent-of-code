;; https://adventofcode.com/2020/day/21
 (ns aoc2020.day21
   (:require
    [aoc.day :as d]
    [aoc.util.string :as s]
    [clojure.set :as set]
    [clojure.string :as str]))

(defn input [] (d/day-input 2020 21))

(defn parse-foods [input]
  (map (fn [line]
         (let [[_ ingredients allergens] (re-matches #"^(.*) \(contains (.*)\)$" line)]
           {:ingredients (set (str/split ingredients #"\s+"))
            :allergens (set (str/split allergens #", "))}))
       (s/lines input)))

(defn remove-allergen-ingredient [foods allergen ingredient]
  (map (fn [food]
         (-> food
             (update :allergens disj allergen)
             (update :ingredients disj ingredient)))
       foods))

(defn find-possible-allergen-ingredients [foods]
  (let [allergens (reduce set/union (map :allergens foods))]
    (reduce (fn [m allergen]
              (let [foods-with-allergen (filter #(contains? (:allergens %) allergen) foods)
                    possible-ingredients (reduce set/intersection (map :ingredients foods-with-allergen))]
                (assoc m allergen possible-ingredients)))
            {}
            allergens)))

(defn find-allergens-ingredient [foods]
  (loop [allergen-map (find-possible-allergen-ingredients foods)]
    (let [determined (keep #(when (= 1 (count (val %))) (key %)) allergen-map)]
      (if (= (count determined) (count allergen-map))
        (into {} (map (fn [[allergen [ingredient & _]]] [allergen ingredient]) allergen-map))
        (recur (reduce (fn [m determined-allergen]
                         (let [ingredient (first (get allergen-map determined-allergen))]
                           (reduce (fn [m [allergen ingredients]]
                                     (if (= allergen determined-allergen)
                                       (assoc m allergen #{ingredient})
                                       (assoc m allergen (disj ingredients ingredient))))
                                   {}
                                   m)))
                       allergen-map
                       determined))))))

(defn find-non-allergen-ingredients [foods]
  (let [determined-allergens (find-allergens-ingredient foods)]
    (mapcat :ingredients
            (reduce (fn [f [allergen ingredient]]
                      (remove-allergen-ingredient f allergen ingredient))
                    foods
                    determined-allergens))))

(defn part1 [input]
  (-> input parse-foods find-non-allergen-ingredients count))

(defn part2 [input]
  (->> input parse-foods find-allergens-ingredient
       (sort-by key)
       (map second)
       (str/join ",")))
