;; https://adventofcode.com/2017/day/16
(ns aoc2017.day16
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2017 16))

(defn spin [x dancers]
  (let [start (- (count dancers) x)]
    (str (subs dancers start) (subs dancers 0 start))))

(defn partner [[a b] dancers]
  (str/join (replace {a b, b a} dancers)))

(defn exchange [[a b] dancers]
  (partner [(nth dancers a) (nth dancers b)] dancers))

(defn parse [input]
  (map (fn [step]
         (case (first step)
           \s (partial spin (s/parse-int (subs step 1)))
           \x (partial exchange (s/parse-ints (subs step 1)))
           \p (partial partner (map first (str/split (subs step 1) #"/")))))
       (str/split input #",")))

(defn dance [steps dancers]
  (reduce (fn [ds step] (step ds)) dancers steps))

(defn dance-forever [steps dancers]
  (iterate (partial dance steps) dancers))

(defn part1
  ([input] (part1 input "abcdefghijklmnop"))
  ([input dancers]
   (dance (parse input) dancers)))

(defn part2 [input]
  (let [dances (dance-forever (parse input) "abcdefghijklmnop")
        ; Find the cycle length and use it to shorten the search.
        [c2 c1] (reduce (fn [seen [i ds]]
                          (if-let [match (seen ds)]
                            (reduced [i match])
                            (assoc seen ds i)))
                        {}
                        (c/indexed dances))]
    (nth dances (mod 1000000000 (- c2 c1)))))

(def t "s1,x3/4,pe/b")
