(ns aoc.util.math)

(defn num-digits [n] (count (str n)))

(defn sum [l] (reduce + l))
(defn product [l] (reduce * l))

(defn distance [x y]
  (abs (- x y)))

(defn manhattan-distance [pos-a pos-b]
  (sum (map distance pos-a pos-b)))

(defn mins-by [n-fn coll]
  (reduce (fn [[mn mins] e]
            (let [n (n-fn e)]
              (case (compare mn n)
                -1 [mn mins]
                0 [mn (conj mins e)]
                1 [n [e]]
                )))
          [Integer/MAX_VALUE []]
          coll))
