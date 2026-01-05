;; https://adventofcode.com/2016/day/21
(ns aoc2016.day21
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2016 21))

(defn- letter-params [s]
  (mapcat second (re-seq #"letter (\w)" s)))

(defn- swap-pos [s x y]
  (let [v (vec s)]
    (str/join (assoc v x (nth v y) y (nth v x)))))

(defn- swap-letter [s x y]
  (let [v (vec s)]
    (str/join (assoc v (c/index v x) y (c/index v y) x))))

(defn- rotate-left  [s n] (str/join (c/rotate-left n s)))
(defn- rotate-right [s n] (str/join (c/rotate-right n s)))

(defn- rotate-letter [s c]
  (let [index (c/index s c)]
    (rotate-right s (+ (inc index) (if (>= index 4) 1 0)))))

(defn- inverse-rotate-letter [s c]
  (->> (range (count s))
       (map #(rotate-left s %))
       (filter #(= s (rotate-letter % c)))
       first))

(defn- reverse-pos [s x y]
  (str (subs s 0 x)
       (str/join (reverse (subs s x (inc y))))
       (subs s (inc y))))

(defn- move-pos [s x y]
  (let [chr (nth s x)
        removed (str (subs s 0 x) (subs s (inc x)))]
    (str (subs removed 0 y) chr (subs removed y))))

(defn- apply-operation [s operation]
  (condp (fn [pre s] (str/starts-with? s pre)) operation
    "swap position" (apply swap-pos s (s/ints operation))
    "swap letter"   (apply swap-letter s (letter-params operation))
    "rotate left"   (apply rotate-left s (s/ints operation))
    "rotate right"  (apply rotate-right s (s/ints operation))
    "rotate based"  (apply rotate-letter s (letter-params operation))
    "reverse"       (apply reverse-pos s (s/ints operation))
    "move"          (apply move-pos s (s/ints operation))))

(defn- reverse-operation [s operation]
  (condp (fn [pre s] (str/starts-with? s pre)) operation
    "swap position" (apply swap-pos s (s/ints operation))
    "swap letter"   (apply swap-letter s (letter-params operation))
    "rotate left"   (apply rotate-right s (s/ints operation))
    "rotate right"  (apply rotate-left s (s/ints operation))
    "rotate based"  (apply inverse-rotate-letter s (letter-params operation))
    "reverse"       (apply reverse-pos s (s/ints operation))
    "move"          (apply move-pos s (reverse (s/ints operation)))))

(defn scramble [s input]
  (reduce apply-operation s (s/lines input)))

(defn un-scramble [s input]
  (reduce reverse-operation s (reverse (s/lines input))))

(defn part1 [input] (scramble "abcdefgh" input))

(defn part2 [input] (un-scramble "fbgdceah" input))
