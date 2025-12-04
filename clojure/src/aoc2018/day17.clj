;; https://adventofcode.com/2018/day/17
(ns aoc2018.day17
  (:require [aoc.day :as d]
            [aoc.util.grid :refer :all]
            [aoc.util.string :as s]
            [aoc.util.vec :refer :all]
            [clojure.string :as str]))

(defn input [] (d/day-input 2018 17))

(defn parse-line [line]
  (let [[axis start end] (s/parse-ints line)]
    (if (str/starts-with? line "x")
      (into {} (for [y (range start (inc end))] [[axis y] \#]))
      (into {} (for [x (range start (inc end))] [[x axis] \#])))))

(defn parse [input]
  (reduce merge (map parse-line (str/split-lines input))))

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
    (let [[_ y :as below] (vec+ end dir-down)]
      (if (or (> y bottom) (blocked? grid below))
        end
        (recur below)))))

(defn floor-edge [grid start dir]
  (first (drop-while #(and (flows? grid %)
                           (blocked? grid (vec+ % dir-down)))
                     (iterate #(vec+ dir %) start))))

(defn drop-water [grid]
  (let [[[_ top] [_ bottom]] (bounds grid)]
    (loop [grid grid, drops #{[500 top]}]
      (if (empty? drops)
        grid
        (let [drop   (first drops)
              drops' (disj drops drop)
              block  (drop-down grid drop bottom)
              grid'  (fill-rect grid drop block \|)]
          (if (nil? (grid' (vec+ block dir-down)))
            (recur grid' drops')
            (let [left-floor       (floor-edge grid' block dir-left)
                  left-floor-open  (flows? grid' left-floor)
                  right-floor      (floor-edge grid' block dir-right)
                  right-floor-open (flows? grid' right-floor)
                  closed           (and (not left-floor-open) (not right-floor-open))
                  both-open        (and left-floor-open right-floor-open)
                  grid'            (fill-rect grid'
                                              (vec+ left-floor dir-right)
                                              (vec+ right-floor dir-left)
                                              (if closed \~ \|))]
              (cond
                closed (recur grid' (conj drops' (vec+ block dir-up)))
                both-open (recur grid' (conj drops' left-floor right-floor))
                left-floor-open (recur grid' (conj drops' left-floor))
                :else (recur grid' (conj drops' right-floor))))))))))

(defn part1 [input]
  (count (locs-where (drop-water (parse input)) #{\| \~})))

(defn part2 [input]
  (count (locs-where (drop-water (parse input)) #{\~})))
