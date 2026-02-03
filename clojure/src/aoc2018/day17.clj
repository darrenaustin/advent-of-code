;; https://adventofcode.com/2018/day/17
(ns aoc2018.day17
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.grid :as g]
   [aoc.util.pos :as p]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2018 17))

(defn parse-line [line]
  (let [[axis start end] (s/ints line)]
    (if (str/starts-with? line "x")
      (into {} (for [y (range start (inc end))] [[axis y] \#]))
      (into {} (for [x (range start (inc end))] [[x axis] \#])))))

(defn parse [input]
  (into (g/->sparse-grid)
        (reduce merge (map parse-line (s/lines input)))))

(defn fill-rect [grid [from-x from-y] [to-x to-y] value]
  (into grid (for [x (range from-x (inc to-x)) y (range from-y (inc to-y))]
               [[x y] value])))

(defn flows? [grid loc]
  (let [cell (grid loc)]
    (or (nil? cell) (= \| cell))))

(defn blocked? [grid loc]
  (#{\# \~} (grid loc)))

(defn drop-down [grid start bottom]
  (loop [end start]
    (let [[_ y :as below] (p/pos+ end p/dir-down)]
      (if (or (> y bottom) (blocked? grid below))
        end
        (recur below)))))

(defn floor-edge [grid start dir]
  (first (drop-while #(and (flows? grid %)
                           (blocked? grid (p/pos+ % p/dir-down)))
                     (iterate #(p/pos+ dir %) start))))

(defn drop-water [grid]
  (let [[[_ top] [_ bottom]] (g/bounds grid)]
    (loop [grid grid, drops #{[500 top]}]
      (if (empty? drops)
        grid
        (let [drop   (first drops)
              drops' (disj drops drop)
              block  (drop-down grid drop bottom)
              grid'  (fill-rect grid drop block \|)]
          (if (nil? (grid' (p/pos+ block p/dir-down)))
            (recur grid' drops')
            (let [left-floor       (floor-edge grid' block p/dir-left)
                  left-floor-open  (flows? grid' left-floor)
                  right-floor      (floor-edge grid' block p/dir-right)
                  right-floor-open (flows? grid' right-floor)
                  closed           (and (not left-floor-open) (not right-floor-open))
                  both-open        (and left-floor-open right-floor-open)
                  grid'            (fill-rect grid'
                                              (p/pos+ left-floor p/dir-right)
                                              (p/pos+ right-floor p/dir-left)
                                              (if closed \~ \|))]
              (cond
                closed (recur grid' (conj drops' (p/pos+ block p/dir-up)))
                both-open (recur grid' (conj drops' left-floor right-floor))
                left-floor-open (recur grid' (conj drops' left-floor))
                :else (recur grid' (conj drops' right-floor))))))))))

(defn part1 [input]
  (count (c/keys-when-val #{\| \~} (drop-water (parse input)))))

(defn part2 [input]
  (count (c/keys-when-val #{\~} (drop-water (parse input)))))
