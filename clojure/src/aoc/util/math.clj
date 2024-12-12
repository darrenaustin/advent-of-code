(ns aoc.util.math)

(defn num-digits [n] (count (str n)))

(defn sum [l] (reduce + l))

(defn distance [x y]
  (abs (- x y)))

(defn manhatten-distance [pos-a pos-b]
  (apply + (map distance pos-a pos-b)))
