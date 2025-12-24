;; https://adventofcode.com/2015/day/20
 (ns aoc2015.day20
   (:require
    [aoc.day :as d]
    [aoc.util.math :as m]
    [aoc.util.string :as s]))

(defn input [] (d/day-input 2015 20))

(defn- presents [presents-per-elf elf-house-limit house]
  (* presents-per-elf
     (reduce + (remove #(> house (* elf-house-limit %))
                       (m/divisors house)))))

(defn- first-house-getting
  ([target presents-per-elf]
   (first-house-getting target presents-per-elf Integer/MAX_VALUE))
  ([target presents-per-elf elf-house-limit]
   (->> (range)
        (map (partial presents presents-per-elf elf-house-limit))
        (take-while (partial > target))
        count)))

;; TODO: very slow, but concise solution
(defn part1 [input] (first-house-getting (s/int input) 10))

(defn part2 [input] (first-house-getting (s/int input) 11 50))
