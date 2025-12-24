;; https://adventofcode.com/2015/day/19
 (ns aoc2015.day19
   (:require
    [aoc.day :as d]
    [aoc.util.string :as s]))

(defn input [] (d/day-input 2015 19))

(defn- parse-rules [input]
  (map (comp vec rest) (re-seq #"(\w+) => (\w+)" input)))

(defn- molecules-from [molecule rules]
  (distinct (for [[element replacement] rules
                  index (s/re-indices element molecule)]
              (s/substring-replace molecule index replacement))))

(defn- min-steps [molecule target sorted-rules]
  ;; with the rules sorted by descending replacement, we can
  ;; reverse replace the rules in the molecule until we are left
  ;; with the target
  (if (= molecule target)
    0
    (some identity
          (for [[r e] sorted-rules ; reverse the replacement with the entry
                index (s/re-indices e molecule)
                :let [steps (min-steps (s/substring-replace molecule index r)
                                       target sorted-rules)]]
            (when steps (inc steps))))))

(defn part1 [input]
  (->> (parse-rules input)
       (molecules-from (last (s/lines input)))
       count))

(defn part2 [input]
  (min-steps (last (s/lines input))
             "e"
             (sort-by (comp count second) > (parse-rules input))))
