;; https://adventofcode.com/2023/day/21
(ns aoc2023.day21
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.grid :as g]
   [aoc.util.pos :as p]))

(defn input [] (d/day-input 2023 21))

(defn- parse-garden [input]
  (let [grid  (g/->sparse-grid input #(when (#{\S \.} %) %))
        start (first (c/keys-when-val #{\S} grid))
        size  (g/width grid)
        plots (set (keys grid))]
    (assert (= (g/width grid) (g/height grid)))
    {:start start
     :size  size
     :plots plots
     :plot? (fn [[x y]] (contains? plots [(mod x size) (mod y size)]))}))

(defn- quadratic? [garden steps]
  (let [{:keys [start size plot?]} garden
        [sx sy] start
        half-size (quot size 2)]
    (and (= sx sy half-size)
         (= (mod steps size) half-size)
         (every? (fn [i] (and (plot? [sx i]) (plot? [i sy]))) (range size)))))

(defn- num-plots [garden targets]
  (let [{:keys [start plot?]} garden]
    (loop [frontier #{start}
           visited  #{start}
           counts   [1 0] ; even/odd counts
           steps    0
           results  []
           targets  targets]
      (cond
        (empty? targets)
        results

        (= steps (first targets))
        (recur frontier visited counts steps (conj results (counts (mod steps 2))) (rest targets))

        :else
        (let [next-frontier (into #{}
                                  (comp (mapcat p/orthogonal-to)
                                        (filter plot?)
                                        (remove visited))
                                  frontier)
              next-steps    (inc steps)]
          (recur next-frontier
                 (into visited next-frontier)
                 (update counts (mod next-steps 2) + (count next-frontier))
                 next-steps
                 results
                 targets))))))

(defn plots-within [input steps]
  (let [garden (parse-garden input)]
    (if (quadratic? garden steps)
      (let [size       (:size garden)
            half-size  (quot size 2)
            [a0 a1 a2] (num-plots garden [half-size (+ half-size size) (+ half-size (* size 2))])
            n          (quot steps size)
            b1         (- a1 a0)
            b2         (- a2 a1)]
        (+ a0
           (* b1 n)
           (* (quot (* n (dec n)) 2) (- b2 b1))))
      (first (num-plots garden [steps])))))

(defn part1 [input] (plots-within input 64))

(defn part2 [input] (plots-within input 26501365))
