;; https://adventofcode.com/2018/day/13
(ns aoc2018.day13
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.grid :as g]
   [aoc.util.pos :as p]
   [clojure.string :as str]))

(defn input [] (d/day-input 2018 13 :trim? false))

(def cart-dir {\^ p/dir-up, \> p/dir-right, \v p/dir-down, \< p/dir-left})

(def cart-track {p/dir-up \|, p/dir-right \-, p/dir-down \|, p/dir-left \-})

(def crossing-next-dir
  {p/turn-left identity, identity p/turn-right, p/turn-right p/turn-left})

(def corner-turn
  {[\/ p/dir-up]    p/dir-right
   [\/ p/dir-right] p/dir-up
   [\/ p/dir-down]  p/dir-left
   [\/ p/dir-left]  p/dir-down
   ;
   [\\ p/dir-up]    p/dir-left
   [\\ p/dir-right] p/dir-down
   [\\ p/dir-down]  p/dir-right
   [\\ p/dir-left]  p/dir-up})

(defn parse [input]
  (let [grid  (g/->grid input)
        carts (map (fn [l] {:loc l :dir (cart-dir (grid l)) :crossing p/turn-left})
                   (c/keys-when-val #{\^ \> \v \<} grid))]
    {:carts  carts
     :tracks (reduce (fn [ts {:keys [loc dir]}] (assoc ts loc (cart-track dir)))
                     (into {} (remove #(#{\space} (second %)) grid))
                     carts)}))

(defn move [tracks {:keys [loc dir crossing] :as cart}]
  (let [loc' (p/pos+ loc dir) track (tracks loc')]
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
