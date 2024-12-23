;; https://adventofcode.com/2024/day/20
(ns aoc2024.day20
  (:require
    [aoc.day :as d]
    [aoc.util.grid :refer :all]
    [aoc.util.math :as m]))

(defn input [] (d/day-input 2024 20))

(defn parse-track [input]
  (let [grid  (parse-grid input)
        start (loc-where grid #{\S})
        end   (loc-where grid #{\E})]
    {:grid  (assoc grid start \. end \.)
     :start start
     :end   end}))

(defn distance-map [{:keys [grid start end]}]
  (let [path (set (locs-where grid #{\.}))]
    (loop [head start dist 1 dists {start 0}]
      (if (= head end)
        dists
        (let [step (first (filter #(and (path %) (not (dists %)))
                                  (orthogonal-from head)))]
          (recur step (inc dist) (assoc dists step dist)))))))


(defn cheat-saves [dists loc cheat]
  (- (dists cheat) (dists loc) (m/manhattan-distance cheat loc)))

(defn num-cheats [input cheat-locs max-savings]
  (let [dists (distance-map (parse-track input))]
    (mapcat (fn [loc]
              (filter #(>= % max-savings)
                      (map #(cheat-saves dists loc %)
                           (filter dists (cheat-locs loc)))))
            (keys dists))))

(defn solve [input min-radius max-radius]
  (count (num-cheats input #(diamond-around min-radius max-radius %) 100)))

(defn part1 [input] (solve input 2 2))

(defn part2 [input] (solve input 2 20))
