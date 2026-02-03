;; https://adventofcode.com/2019/day/17
(ns aoc2019.day17
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.grid :as g]
   [aoc.util.math :as m]
   [aoc.util.pos :as p]
   [aoc2019.intcode :as ic]
   [clojure.string :as str]))

(defn input [] (d/day-input 2019 17))

(def ^:private dirs {\^ p/dir-up, \v p/dir-down, \< p/dir-left, \> p/dir-right})

(def ^:private turns
  {[p/dir-up p/dir-left]    \L
   [p/dir-up p/dir-right]   \R
   [p/dir-right p/dir-up]   \L
   [p/dir-right p/dir-down] \R
   [p/dir-down p/dir-right] \L
   [p/dir-down p/dir-left]  \R
   [p/dir-left p/dir-down]  \L
   [p/dir-left p/dir-up]    \R})

(defn- parse [input]
  (let [grid      (g/->grid (str/join (map char (:output (ic/run (ic/parse-program input))))))
        robot-loc (first (c/keys-when-val #{\^ \v \< \>} grid))
        robot-dir (dirs (grid robot-loc))]
    {:grid  (assoc grid robot-loc \#)
     :robot {:loc robot-loc, :dir robot-dir}}))

(defn- find-path [{:keys [grid robot]}]
  (loop [loc (:loc robot), dir (:dir robot), steps 0, path []]
    (let [forward (p/pos+ loc dir)]
      (if (= \# (grid forward))
        (recur forward dir (inc steps) path)
        (if-let [turn (c/first-where #(= \# (grid (p/pos+ loc %)))
                                     [(p/turn-left dir) (p/turn-right dir)])]
          (if (zero? steps)
            (recur loc turn steps (conj path (turns [dir turn])))
            (recur loc turn 0 (conj path steps (turns [dir turn]))))
          (conj path steps))))))

(defn- matches [pattern coll]
  (let [size (count pattern)]
    (set (for [i (range 0 (inc (- (count coll) size)))
               :when (= pattern (subvec coll i (+ i size)))]
           i))))

(defn- movements-available-for [path]
  (let [patterns (for [start  (range 0 (count path) 2)
                       length (range 2 20 2)
                       :when (< (+ start length) (count path))
                       :let [pattern (subvec path start (+ start length))]
                       :when (>= 20 (count (str/join "," pattern)))]
                   pattern)]
    (loop [[pattern & patterns] patterns, movements {}, seen #{}]
      (cond
        (nil? pattern) movements
        (seen pattern) (recur patterns movements seen)
        :else (let [xs         (matches pattern path)
                    movements' (reduce (fn [m x] (update m x conj pattern))
                                       movements xs)]
                (recur patterns movements' (conj seen pattern)))))))

(defn- update-routines [fns move]
  (or
   (some #(when (= (fns %) move) (update fns :main conj %)) [:A :B :C])
   (some #(when (nil? (fns %)) (-> fns
                                   (assoc % move)
                                   (update :main conj %))) [:A :B :C])))

(defn- movement-routines [path]
  (let [movements (movements-available-for path)
        routines  (fn routines [pos {:keys [main] :as fns}]
                    (if (and (>= pos (count path))
                             (>= 20 (count (str/join "," (map name main)))))
                      fns
                      (loop [[move & moves] (movements pos)]
                        (when move
                          (if-let [fns' (update-routines fns move)]
                            (if-let [valid-move (routines (+ pos (count move)) fns')]
                              valid-move
                              (recur moves))
                            (recur moves))))))]
    (routines 0 {:main []})))

(defn- ascii-list [str]
  (apply list (map int str)))

(defn sum-intersections [grid]
  (m/sum
   (map m/product
        (filter #(and (= \# (grid %))
                      (every? #{\#} (map grid (p/orthogonal-to %))))
                (keys grid)))))

(defn part1 [input] (sum-intersections (:grid (parse input))))

(defn part2 [input]
  (let [path    (find-path (parse input))
        fns     (movement-routines path)
        program (assoc (ic/parse-program input) 0 2)
        input   (ascii-list
                 (str/join "\n"
                           [(str/join "," (map name (:main fns)))
                            (str/join "," (:A fns))
                            (str/join "," (:B fns))
                            (str/join "," (:C fns))
                            "n" ""]))]
    (last (:output (ic/run program input [])))))
