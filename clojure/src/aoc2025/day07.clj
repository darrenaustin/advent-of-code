;; https://adventofcode.com/2025/day/7
 (ns aoc2025.day07
   (:require
    [aoc.day :as d]
    [aoc.util.math :as m]
    [aoc.util.string :as s]))

(defn input [] (d/day-input 2025 7))

;; I had a previous solution that worked well (see git history),
;; but I really liked the solution that HexHowells posted on reddit:
;;
;; https://www.reddit.com/r/adventofcode/comments/1pg9w66/comment/nspzwt4
;;
;; This is my less concise Clojure version of it:

(defn split-timelines [input]
  (let [[line & lines] (s/lines input)
        row (mapv #(if (= % \S) 1 0) line)]
    (loop [row row splits 0 [line & lines] lines]
      (if line
        (let [splitters (filter #(and (= (get line %) \^)
                                      (not= (get row %) 0))
                                (range (count line)))
              row' (reduce (fn [r s] (let [beam (get r s 0)]
                                       (-> r
                                           (assoc s 0)
                                           (update-in [(dec s)] + beam)
                                           (update-in [(inc s)] + beam))))
                           row splitters)]
          (recur row' (+ splits (count splitters)) lines))
        ;; number of splits, number of timelines
        [splits (m/sum row)]))))

(defn part1 [input] (first (split-timelines input)))

(defn part2 [input] (second (split-timelines input)))
