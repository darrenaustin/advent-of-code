;; https://adventofcode.com/2025/day/6
 (ns aoc2025.day06
   (:require
    [aoc.day :as d]
    [aoc.util.collection :as c]
    [aoc.util.math :as m]
    [aoc.util.string :as s]
    [clojure.string :as str]))

(defn input [] (d/day-input 2025 6))

(defn solve [operator numbers]
  (case operator
    \+ (apply + numbers)
    \* (apply * numbers)))

(defn part1 [input]
  (let [grid (mapv vec (str/split-lines input))
        operators (remove #{\space} (last grid))
        numbers (c/transpose (mapv #(s/parse-ints (str/join %)) (butlast grid)))]
    (m/sum (map solve operators numbers))))

(defn part2 [input]
  (let [grid (mapv vec (str/split-lines input))
        operators (remove #{\space} (last grid))
        numbers (remove #{[nil]}
                        (partition-by nil?
                                      (mapv #(s/parse-int (str/join %))
                                            (c/transpose (butlast grid)))))]
    (m/sum (map solve operators numbers))))
