;; https://adventofcode.com/2015/day/15
 (ns aoc2015.day15
   (:require
    [aoc.day :as d]
    [aoc.util.combo :as combo]
    [aoc.util.string :as s]))

(defn input [] (d/day-input 2015 15))

(defn parse-ingredients [input]
  (map s/ints (s/lines input)))

(defn solve [input cals-pred]
  (let [ingredients (parse-ingredients input)
        n (count ingredients)
        ;; Transpose to get lists of properties: (caps durs flavs texs cals)
        props (apply map list ingredients)
        prop-lists (drop-last props)
        cal-list (last props)]
    (transduce
     (map (fn [amounts]
            (let [dot (fn [v1]
                        (loop [s 0, v1 v1, v2 amounts]
                          (if v1
                            (recur (+ s (* (first v1) (first v2))) (next v1) (next v2))
                            s)))
                  cals (dot cal-list)]
              (if (cals-pred cals)
                (loop [ps prop-lists, acc 1]
                  (if ps
                    (let [s (dot (first ps))]
                      (if (pos? s)
                        (recur (next ps) (* acc s))
                        0))
                    acc))
                0))))
     max
     0
     (combo/weak-compositions n 100))))

(defn part1 [input] (solve input (constantly true)))

(defn part2 [input] (solve input #(= 500 %)))

;; Original more idiomatic solution. It ran in ~1.5s for each part
;; which was a little sluggish. The above solution removes a lot of
;; the intermediate sequence generation by using a transducer, and
;; manually looping for the various computations.

;; (defn cookie [ingredients amounts]
;;   (apply map + (map (fn [i a] (map #(* a %) i)) ingredients amounts)))

;; (defn calories [cookie]
;;   (last cookie))

;; (defn score [cookie]
;;   (reduce * (map #(max 0 %) (drop-last cookie))))

;; (defn possible-cookies [ingredients]
;;   (map #(cookie ingredients %)
;;        (combo/weak-compositions (count ingredients) 100)))

;; (defn part1 [input]
;;   (->> (parse-ingredients input)
;;        possible-cookies
;;        (map score)
;;        (apply max)))

;; (defn part2 [input]
;;   (->> (parse-ingredients input)
;;        possible-cookies
;;        (filter #(= 500 (calories %)))
;;        (map score)
;;        (apply max)))
