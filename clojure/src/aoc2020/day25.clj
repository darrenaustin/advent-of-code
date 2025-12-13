;; https://adventofcode.com/2020/day/25
  (ns aoc2020.day25
    (:require
     [aoc.day :as d]
     [aoc.util.string :as s]))

(defn input [] (d/day-input 2020 25))

(defn transform [subject n] (rem (* n subject) 20201227))

(defn loop-size [key subject]
  (loop [n 1 loops 0]
    (if (= n key)
      loops
      (recur (transform subject n) (inc loops)))))

(defn part1 [input]
  (let [[key1 key2] (s/ints input)
        loop-size1 (loop-size key1 7)]
    (nth (iterate (partial transform key2) 1) loop-size1)))

(defn part2 [_] "🎄 Got em all! 🎉")
