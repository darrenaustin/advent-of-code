;; https://adventofcode.com/2025/day/7
 (ns aoc2025.day07
   (:require
    [aoc.day :as d]
    [aoc.util.grid :as g]
    [aoc.util.vec :as v]))

(defn input [] (d/day-input 2025 7))

(defn part1 [input]
  (let [manifold (g/parse-grid input)
        entrance (g/loc-where manifold #{\S})]
    (loop [[beams splits] [[entrance] 0]]
      (if (empty? beams)
        splits
        (recur (reduce (fn [[b s] beam]
                         (let [down (v/vec+ beam v/dir-down)
                               cell (manifold down)]
                           (case cell
                             nil [b s]
                             \.  [(conj b down) s]
                             \^ (let [new-beams [(v/vec+ down v/dir-left)
                                                 (v/vec+ down v/dir-right)]]
                                  [(apply conj b (remove #(nil? (manifold %)) new-beams))
                                   (inc s)]))))
                       [#{} splits] beams))))))

(def num-quantum-timelines
  (memoize
   (fn [manifold start]
     (if (manifold start)
       (let [down (v/vec+ start v/dir-down)
             cell (manifold down)]
         (case cell
           nil 1
           \. (num-quantum-timelines manifold down)
           \^ (+ (num-quantum-timelines manifold (v/vec+ down v/dir-left))
                 (num-quantum-timelines manifold (v/vec+ down v/dir-right)))))
       0))))

(defn part2 [input]
  (let [manifold (g/parse-grid input)]
    (num-quantum-timelines manifold (g/loc-where manifold #{\S}))))
