;; https://adventofcode.com/2022/day/24
(ns aoc2022.day24
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.grid :as g]
   [clojure.data.int-map :refer [dense-int-set]]
   [clojure.set :as set]))

(defn input [] (d/day-input 2022 24))

(def ^:private ^:const coord-range 256)
(def ^:private ^:const dir-up (- coord-range))
(def ^:private ^:const dir-down coord-range)
(def ^:private ^:const dir-left -1)
(def ^:private ^:const dir-right 1)

(defn- coord ^long [^long x ^long y] (+ x (* coord-range y)))

(defn- build-valley [grid]
  (let [[w h] (g/size grid)]
    (reduce
     (fn [v [x y :as p]]
       (let [p' [(dec x) (dec y)]]
         (case (grid p)
           \> (update v :blizzards conj [p' [1 0]])
           \< (update v :blizzards conj [p' [-1 0]])
           \^ (update v :blizzards conj [p' [0 -1]])
           \v (update v :blizzards conj [p' [0 1]])
           \# (update v :walls conj (apply coord p'))
           v)))
     {:blizzards [], :walls (dense-int-set), :width (- w 2), :height (- h 2)}
     (for [y (range h) x (range w)] [x y]))))

(defn- parse-valley [input]
  (let [{:keys [walls width height] :as valley} (build-valley (g/->grid input))
        entrance (c/first-where (complement walls) (for [x (range -1 width)] (coord x -1)))
        exit (c/first-where (complement walls) (for [x (range -1 width)] (coord x height)))]
    (-> valley
        (assoc :entrance entrance)
        (assoc :exit exit)
        (update :walls conj (+ entrance dir-up))
        (update :walls conj (+ exit dir-down)))))

(defn- blizzards-at [blizzards ^long w ^long h ^long time]
  (map (fn [[[^long x ^long y] [^long dx ^long dy]]]
         (coord (mod (+ x (* dx time)) w)
                (mod (+ y (* dy time)) h)))
       blizzards))

(defn- neighbors [^long pos]
  [(+ pos dir-up) (+ pos dir-down) (+ pos dir-left) (+ pos dir-right)])

(defn- min-steps [{:keys [blizzards walls width height]} start goal time]
  (reduce (fn [current time]
            (if (current goal)
              (reduced time)
              (let [moves (into current (mapcat neighbors current))
                    blocked (into walls (blizzards-at blizzards width height (inc time)))]
                (set/difference moves blocked))))
          (dense-int-set [start])
          (iterate inc time)))

(defn part1 [input]
  (let [valley (parse-valley input)]
    (min-steps valley (:entrance valley) (:exit valley) 0)))

(defn part2 [input]
  (let [valley (parse-valley input)]
    (->> (min-steps valley (:entrance valley) (:exit valley) 0)
         (min-steps valley (:exit valley) (:entrance valley))
         (min-steps valley (:entrance valley) (:exit valley)))))
