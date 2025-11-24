;; https://adventofcode.com/2017/day/20
(ns aoc2017.day20
  (:require [aoc.day :as d]
            [aoc.util.collection :refer [indexed]]
            [aoc.util.grid :refer :all]
            [aoc.util.math :as m]
            [aoc.util.string :as s]
            [clojure.set :as set]))

(defn input [] (d/day-input 2017 20))

(defn parse [input]
  (mapv #(mapv vec (partition 3 %)) (partition 9 (s/parse-ints input))))

(defn origin-distance [v]
  (m/manhattan-distance origin3 v))

; Because this is not continuous the formula for position is:
; p(t) = p + v*t + a*(t*(t+1)/2) =>  p + (v + a/2)*t + (a/2)t^2
;
; So for them to collide the equation is:
; p0x + (v0x + a0x/2)t + (a0x/2)t^2 = p1x + (v1x + a1x/2)t + (a1x/2)t^2
;
; ((a1x - a0x)/2) t^2 + ((vx1 + ax1/2) - (v0x + a0x/2))t + (p1x - p0x)
(defn axis-roots [p0 p1 axis-idx]
  (let [[p0x v0x a0x] (map #(nth % axis-idx) p0)
        [p1x v1x a1x] (map #(nth % axis-idx) p1)
        a (/ (- a1x a0x) 2)
        b (- (+ v1x (/ a1x 2)) (+ v0x (/ a0x 2)))
        c (- p1x p0x)
        roots (m/quadratic-roots a b c)]
    (if (= :any roots)
      roots
      (set (map int (filter m/eq-pos-int? roots))))))

(defn collision [p0 p1]
  (let [roots (mapv #(axis-roots p0 p1 %) (range 3))]
    (first (sort (apply set/intersection (remove #{:any} roots))))))

(defn part1 [input]
  (first
   (apply min-key second
          (indexed (map (fn [[_ _ a]] (origin-distance a))
                        (parse input))))))

;; A much faster solution to part 2 that just runs the simulation looking for
;; collisions. However, it stops at an arbitrary step which works for
;; my input, but it isn't guaranteed.
;
;(defn tick [[p v a]]
;  (let [v' (vec+ v a) p' (vec+ p v')]
;    [p' v' a]))
;
;(defn part2 [input]
;  (let [particles (parse input)]
;    (loop [alive (set particles) steps 1]
;      (if (> steps 1000)
;        (count alive)
;        (let [alive'     (set (map tick alive))
;              collisions (set
;                           (apply concat
;                                  (filter #(> (count %) 1)
;                                          (vals (group-by first  alive')))))]
;          (recur (set/difference alive' collisions) (inc steps)))))))

(defn- remove-pairs-group [alive pairs]
  (reduce (fn [a [p0 p1]] (if (and (alive p0) (alive p1))
                            (disj a p0 p1)
                            a))
          alive
          pairs))

(defn- collision-schedule [particles]
  (remove nil? (for [i (range (dec (count particles)))
                     j (range (inc i) (count particles))]
                 (let [p0 (particles i)
                       p1 (particles j)
                       t  (collision p0 p1)]
                   (when t [t [p0 p1]])))))

(defn part2 [input]
  (let [particles  (parse input)
        collisions (map second
                        (sort-by first
                                 (reduce (fn [s [t p]] (update s t conj p))
                                         {}
                                         (collision-schedule particles))))]
    (count (reduce remove-pairs-group (set particles) collisions))))
