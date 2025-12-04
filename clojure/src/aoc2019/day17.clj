;; https://adventofcode.com/2019/day/17
(ns aoc2019.day17
  (:require [aoc.day :as d]
            [aoc.util.collection :as c]
            [aoc.util.grid :as g]
            [aoc.util.math :as m]
            [aoc.util.vec :as v]
            [aoc2019.intcode :as i]
            [clojure.string :as str]))

(defn input [] (d/day-input 2019 17))

(def dirs {\^ v/dir-up, \v v/dir-down, \< v/dir-left, \> v/dir-right})

(def turns
  {[v/dir-up v/dir-left]    \L
   [v/dir-up v/dir-right]   \R
   [v/dir-right v/dir-up]   \L
   [v/dir-right v/dir-down] \R
   [v/dir-down v/dir-right] \L
   [v/dir-down v/dir-left]  \R
   [v/dir-left v/dir-down]  \L
   [v/dir-left v/dir-up]    \R})

(defn parse [input]
  (let [grid      (g/parse-grid (str/join (map char (:output (i/run (i/parse input))))))
        robot-loc (g/loc-where grid #{\^ \v \< \>})
        robot-dir (dirs (grid robot-loc))]
    {:grid  (assoc grid robot-dir \#)
     :robot {:loc robot-loc, :dir robot-dir}}))

(defn sum-intersections [grid]
  (m/sum
   (map m/product (filter #(and (= \# (grid %))
                                (every? #{\#} (map grid (v/orthogonal-from %))))
                          (keys grid)))))

(defn find-path [{:keys [grid robot]}]
  (loop [loc (:loc robot), dir (:dir robot), steps 0, path []]
    (let [forward (v/vec+ loc dir)]
      (if (= \# (grid forward))
        (recur forward dir (inc steps) path)
        (if-let [turn (c/first-where #(= \# (grid (v/vec+ loc %)))
                                     [(v/ortho-turn-left dir) (v/ortho-turn-right dir)])]
          (if (zero? steps)
            (recur loc turn steps (conj path (turns [dir turn])))
            (recur loc turn 0 (conj path steps (turns [dir turn]))))
          (conj path steps))))))

(defn matches [pattern coll]
  (let [size (count pattern)]
    (set (for [i (range 0 (inc (- (count coll) size)))
               :when (= pattern (subvec coll i (+ i size)))]
           i))))

(defn movements-available-for [path]
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

(defn update-routines [fns move]
  (or
   (some #(when (= (fns %) move) (update fns :main conj %)) [:A :B :C])
   (some #(when (nil? (fns %)) (-> fns
                                   (assoc % move)
                                   (update :main conj %))) [:A :B :C])))

(defn movement-routines [path]
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

(defn ascii-list [str]
  (apply list (map int str)))

(defn part1 [input] (sum-intersections (:grid (parse input))))

(defn part2 [input]
  (let [path    (find-path (parse input))
        fns     (movement-routines path)
        program (assoc (i/parse input) 0 2)
        input   (ascii-list
                 (str/join "\n"
                           [(str/join "," (map name (:main fns)))
                            (str/join "," (:A fns))
                            (str/join "," (:B fns))
                            (str/join "," (:C fns))
                            "n" ""]))]
    (last (:output (i/run program input [])))))
