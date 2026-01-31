;; https://adventofcode.com/2022/day/15
(ns aoc2022.day15
  (:require
   [aoc.day :as d]
   [aoc.util.math :as m]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2022 15))

(defn- parse-sensors [input]
  (->> (s/ints input)
       (partition 4)
       (map (fn [[sx sy bx by]]
              {:sensor [sx sy]
               :beacon [bx by]
               :distance (int (m/manhattan-distance [sx sy] [bx by]))}))))

(defn- y-range [[sx sy] distance y]
  (let [dy (abs (- y sy))]
    (when (<= dy distance)
      (let [dx (- distance dy)]
        [(- sx dx) (+ sx dx)]))))

(defn- merge-sorted-ranges [ranges]
  (reduce
   (fn [rs [s e]]
     (if-let [[s1 e1] (peek rs)]
       (if (<= s (inc e1))
         (conj (pop rs) [s1 (max e1 e)])
         (conj rs [s e]))
       [[s e]]))
   []
   ranges))

(defn- range-size [[s e]] (inc (- e s)))

(defn- beacons-on [y sensors]
  (->> (map :beacon sensors)
       (filter #(= y (second %)))
       (map first)
       distinct
       count))

(defn no-beacons [input y]
  (let [sensors (parse-sensors input)
        ranges  (->> sensors
                     (keep #(y-range (:sensor %) (:distance %) y))
                     sort
                     merge-sorted-ranges)
        covered (transduce (map range-size) + 0 ranges)
        beacons (beacons-on y sensors)]
    (- covered beacons)))

(defn- perimeter-intercepts
  "Returns the y-intercepts of the four lines bounding the diamond just outside the sensor's range.
   Returns a map {:pos [b1 b2] :neg [b3 b4]} where:
   - :pos are intercepts for lines with slope +1 (y = x + b)
   - :neg are intercepts for lines with slope -1 (y = -x + b)"
  [{:keys [sensor distance]}]
  (let [[sx sy] sensor
        d (inc distance)]
    {:pos [(- sy sx d) (- (+ sy d) sx)]
     :neg [(+ sy sx d) (+ (- sy d) sx)]}))

(defn- intersection-point [b-pos b-neg]
  (let [sum (+ b-neg b-pos)
        diff (- b-neg b-pos)]
    (when (even? sum)
      [(/ diff 2) (/ sum 2)])))

(defn- unseen? [sensors pos]
  (every? #(> (m/manhattan-distance (:sensor %) pos) (:distance %)) sensors))

(defn- in-bounds? [[x y] max-coord] (and (<= 0 x max-coord) (<= 0 y max-coord)))

(defn tuning-frequency [[x y]] (+ (* x 4000000) y))

(defn- find-distress-beacon [sensors max-coord]
  (let [intercepts (map perimeter-intercepts sensors)
        pos-lines  (mapcat :pos intercepts)
        neg-lines  (mapcat :neg intercepts)
        candidates (eduction
                    (mapcat (fn [p] (keep #(intersection-point p %) neg-lines)))
                    (filter #(in-bounds? % max-coord))
                    (filter #(unseen? sensors %))
                    pos-lines)]
    (first candidates)))

(defn distress-beacon-freq [input max-coord]
  (-> (parse-sensors input)
      (find-distress-beacon max-coord)
      tuning-frequency))

(defn part1 [input] (no-beacons input 2000000))

(defn part2 [input] (distress-beacon-freq input 4000000))
