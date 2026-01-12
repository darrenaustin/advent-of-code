;; https://adventofcode.com/2018/day/5
(ns aoc2018.day05
  (:require
   [aoc.day :as d]
   [aoc.util.char :as char]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2018 5))

;; Cheeky, but very slow regex solution:
;;
;; (def ^:private react-regex
;;   (re-pattern (str/join "|" (mapcat (fn [a A] [(str a A) (str A a)])
;;                                     s/alphabet-lower s/alphabet-upper))))
;;
;; (defn- reduce-polymer [polymer]
;;   (c/first-duplicate (iterate #(str/replace % react-regex "") polymer)))

(defn- reaction? [u1 u2]
  (and u1 u2
       (not= u1 u2)
       (= (char/lower-case ^Character u1)
          (char/lower-case ^Character u2))))

(defn- reduce-polymer [polymer]
  (reduce
   (fn [chain unit]
     (let [prev (peek chain)]
       (if (reaction? prev unit)
         (pop chain)
         (conj chain unit))))
   []
   polymer))

(defn- remove-unit [polymer unit]
  (remove #{unit (char/upper-case unit)} polymer))

(defn part1 [input] (count (reduce-polymer input)))

(defn part2 [input]
  (->> s/alphabet-lower
       (pmap (fn [unit]
               (-> input
                   (remove-unit unit)
                   (reduce-polymer)
                   count)))
       (apply min)))
