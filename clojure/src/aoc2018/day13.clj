;; https://adventofcode.com/2018/day/13
(ns aoc2018.day13
  (:require [aoc.day :as d]
            [aoc.util.collection :as c]
            [aoc.util.grid :refer :all]
            [aoc.util.vec :refer :all]
            [clojure.string :as str]))

;; Need to ensure the input isn't trimmed as
;; it will remove important spacing in the grid.
(defn input [] (d/day-input 2018 13 false))

(def cart-dir {\^ dir-up, \> dir-right, \v dir-down, \< dir-left})

(def cart-track {dir-up \|, dir-right \-, dir-down \|, dir-left \-})

(def turn-right
  {dir-up dir-right, dir-right dir-down, dir-down dir-left, dir-left dir-up})

(def turn-left (c/val->key turn-right))

(def crossing-next-dir
  {turn-left identity, identity turn-right, turn-right turn-left})

(def corner-turn
  {[\/ dir-up]    dir-right
   [\/ dir-right] dir-up
   [\/ dir-down]  dir-left
   [\/ dir-left]  dir-down
   ;
   [\\ dir-up]    dir-left
   [\\ dir-right] dir-down
   [\\ dir-down]  dir-right
   [\\ dir-left]  dir-up})

(defn parse [input]
  (let [grid  (parse-grid input)
        carts (map (fn [l] {:loc l :dir (cart-dir (grid l)) :crossing turn-left})
                   (locs-where grid #{\^ \> \v \<}))]
    {:carts  carts
     :tracks (reduce (fn [ts {:keys [loc dir]}] (assoc ts loc (cart-track dir)))
                     (into {} (remove #(#{\space} (second %)) grid))
                     carts)}))

(defn move [tracks {:keys [loc dir crossing] :as cart}]
  (let [loc' (vec+ loc dir) track (tracks loc')]
    (cond
      (= track (cart-track dir)) (assoc cart :loc loc')
      (= track \+) {:loc loc' :dir (crossing dir) :crossing (crossing-next-dir crossing)}
      :else {:loc loc' :dir (corner-turn [track dir]) :crossing crossing})))

(defn collision [loc carts]
  ((set (map :loc carts)) loc))

(defn remove-carts [loc carts]
  (remove (comp #{loc} :loc) carts))

(defn tick [{:keys [tracks carts]}]
  (let [carts (sort-by (comp vec reverse :loc) carts)]
    (loop [[cart & to-move] carts, moved [], collisions []]
      (if (nil? cart)
        {:tracks tracks :carts moved :collisions collisions}
        (let [cart'   (move tracks cart)
              collide (or (collision (:loc cart') moved)
                          (collision (:loc cart') to-move))]
          (if collide
            (recur
             (remove-carts collide to-move)
             (remove-carts collide moved)
             (conj collisions collide))
            (recur to-move (conj moved cart') collisions)))))))

(defn solve [input finished-fn]
  (str/join ","
            (loop [system (parse input)]
              (let [system' (tick system)]
                (or (finished-fn system')
                    (recur system'))))))

(defn first-collision [system]
  (first (:collisions system)))

(defn last-cart-standing [system]
  (when (= 1 (count (:carts system)))
    (:loc (first (:carts system)))))

(defn part1 [input] (solve input first-collision))

(defn part2 [input] (solve input last-cart-standing))
