;; https://adventofcode.com/2016/day/17
    (ns aoc2016.day17
      (:require
       [aoc.day :as d]
       [aoc.util.collection :as c]
       [aoc.util.pos :as p]
       [pandect.algo.md5 :refer [md5]]))

(defn input [] (d/day-input 2016 17))

(def ^:private dirs
  [[p/dir-up \U]
   [p/dir-down \D]
   [p/dir-left \L]
   [p/dir-right \R]])

(defn- valid-pos? [[x y]] (and (<= 0 x 3) (<= 0 y 3)))

(defn- unlocked? [h] (#{\b \c \d \e \f} h))

(defn- goal? [[_ pos]] (= pos [3 3]))

(defn- de-salt [salt path] (subs path (count salt)))

(defn- open-neighbors [[path pos]]
  (let [doors (subs (md5 path) 0 4)
        moves (keep-indexed #(when (unlocked? %2) (dirs %1)) doors)]
    (for [[dir code] moves
          :let [pos' (p/pos+ pos dir)]
          :when (valid-pos? pos')]
      [(str path code) pos'])))

(defn- all-paths [passcode]
  ;; Given the small search space this will do a BFS, returning
  ;; a lazy sequence of all valid paths, sorted by length.
  (let [step
        (fn step [q]
          (lazy-seq
           (when (seq q)
             (let [current (peek q)]
               (if (goal? current)
                 (cons (de-salt passcode (first current)) (step (pop q)))
                 (step (into (pop q) (open-neighbors current))))))))]
    (step (conj c/empty-queue [passcode [0 0]]))))

(defn part1 [input] (first (all-paths input)))

(defn part2 [input] (count (last (all-paths input))))
