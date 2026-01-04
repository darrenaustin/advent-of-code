;; https://adventofcode.com/2016/day/19
 (ns aoc2016.day19
   (:require
    [aoc.day :as d]
    [aoc.util.string :as s]
    [clojure.math :as math]))

(defn input [] (d/day-input 2016 19))

;; Josephus problem:
;; https://en.wikipedia.org/wiki/Josephus_problem#Solution
(defn part1 [input]
  (let [n (s/int input)]
    (inc (* 2 (- n (Integer/highestOneBit n))))))

;; For part 2, for every step we take, we kill one person. But the "distance"
;; to the person we kill is n / 2. As we go around the circle, we effectively
;; skip 1 person and kill 1 person (roughly) relative to the shrinking size,
;; but the geometry is such that the "safe" spot rotates 3 times faster than
;; the circle shrinks. Therefore it is dictated by powers of 3.
(defn part2 [input]
  (let [n (s/int input)
        ;; Find w, the largest power of 3 <= n
        w (long (math/pow 3 (int (/ (math/log n) (math/log 3)))))]
    (if (= n w)
      n
      (let [rem (- n w)]
        (if (<= rem w)
          ;; Slow rise: n <= 2w, result is n - w
          rem
          ;; Fast rise: n > 2w, result is 2(n - w) - w = 2n - 3w
          (- (* 2 rem) w))))))
