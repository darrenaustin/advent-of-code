;; https://adventofcode.com/2022/day/14
 (ns aoc2022.day14
   (:require
    [aoc.day :as d]
    [aoc.util.collection :as c]
    [aoc.util.pos :as p]
    [aoc.util.string :as s]))

(defn input [] (d/day-input 2022 14))

(def ^:private sand-entry [500 0])

(defn- parse-paths [input]
  (map #(partition 2 (s/ints %)) (s/lines input)))

(defn- path->coords [path]
  (mapcat (fn [[[x1 y1] [x2 y2]]]
            (let [xs (range (min x1 x2) (inc (max x1 x2)))
                  ys (range (min y1 y2) (inc (max y1 y2)))]
              (for [x xs, y ys] [x y])))
          (partition 2 1 path)))

(defn- cave-rocks [input]
  (set (mapcat path->coords (parse-paths input))))

(defn- cave-depth [rocks]
  (transduce (map second) max 0 rocks))

(defn- drop-grain [fall-limit obstacles]
  (loop [grain sand-entry]
    (when-not (or (>= (second grain) fall-limit) (obstacles grain))
      (if-let [next-pos (->> [p/dir-s p/dir-sw p/dir-se]
                             (map #(p/pos+ grain %))
                             (remove obstacles)
                             first)]
        (recur next-pos)
        grain))))

(defn- drop-sand [rocks]
  (let [fall-limit (inc (cave-depth rocks))]
    (loop [obstacles rocks, count 0]
      (if-let [settled (drop-grain fall-limit obstacles)]
        (recur (conj obstacles settled) (inc count))
        count))))

(defn- flood-fill-sand [rocks floor-y]
  (loop [q (c/queue sand-entry) grains #{sand-entry}]
    (if-let [[cx cy] (peek q)]
      (let [q (pop q)
            next-y (inc cy)]
        (if (>= next-y floor-y)
          (recur q grains)
          (let [candidates (eduction
                            (map (fn [dx] [(+ cx dx) next-y]))
                            (remove #(or (rocks %) (grains %)))
                            [0 -1 1])]
            (recur (into q candidates) (into grains candidates)))))
      (count grains))))

(defn part1 [input] (drop-sand (cave-rocks input)))

(defn part2 [input]
  (let [rocks (cave-rocks input)]
    (flood-fill-sand rocks (+ 2 (cave-depth rocks)))))
