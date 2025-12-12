;; https://adventofcode.com/2018/day/23
(ns aoc2018.day23
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [aoc.util.pos :as p]
   [clojure.data.priority-map :refer [priority-map]]))

(defn input [] (d/day-input 2018 23))

(defn parse [input]
  (for [[x y z r] (partition 4 (s/parse-ints input))]
    [[x y z] r]))

(defn in-range? [[a r] [b _]]
  (>= r (m/manhattan-distance a b)))

(defn box-intersect? [[box-min box-max] [bot-center bot-radius]]
  (<= (quot (m/sum
             (for [a [0 1 2]
                   :let [low  (box-min a)
                         high (dec (box-max a))]]
               (+ (abs (- (bot-center a) low))
                  (abs (- (bot-center a) high))
                  (- high)
                  low)))
            2)
      bot-radius))

(defn num-intersect [box bots]
  (c/count-where (partial box-intersect? box) bots))

(defn sub-divide [[box-min box-max]]
  (let [side (quot (abs (- (box-max 0) (box-min 0))) 2)]
    (if (zero? side)
      [[box-min (mapv inc box-min)]]
      (for [d [[0 0 0] [0 0 1] [0 1 0] [0 1 1] [1 0 0] [1 0 1] [1 1 0] [1 1 1]]
            :let [box-min' (p/pos+ box-min (p/pos*n side d))
                  box-max' (p/pos+ box-min' [side side side])]]
        [box-min' box-max']))))

(defn max-extent [bots]
  (apply max (mapcat (fn [[pos r]] (map #(+ r (abs %)) pos)) bots)))

(defn bounding-box [bots]
  (let [max-dimen (max-extent bots)
        side      (first (drop-while #(<= % max-dimen) (iterate #(* 2 %) 1)))]
    [[(- side) (- side) (- side)] [side side side]]))

(defn part1 [input]
  (let [bots      (parse input)
        strongest (apply max-key second bots)]
    (c/count-where (partial in-range? strongest) bots)))

(defn part2 [input]
  (let [bots (parse input)]
    (loop [open (priority-map (bounding-box bots) [0 0])]
      (let [[[box-min box-max :as box] [_ dist]] (peek open)
            side (- (box-max 0) (box-min 0))]
        (if (= 1 side)
          dist
          (recur (into (pop open)
                       (for [box' (sub-divide box)]
                         [box' [(- (num-intersect box' bots))
                                (m/manhattan-distance [0 0 0] (first box'))]]))))))))
