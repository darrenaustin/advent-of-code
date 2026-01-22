;; https://adventofcode.com/2022/day/7
(ns aoc2022.day07
  (:require
   [aoc.day :as d]
   [aoc.util.math :refer [sum]]
   [aoc.util.memoize :refer [letfn-mem]]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2022 7))

(defn- path-for [path dir-name]
  (conj (or path []) dir-name))

(defn- cd [cwd dir-name]
  (case dir-name
    "/" ["/"]
    ".." (pop cwd)
    (path-for cwd dir-name)))

(defn- process-line [fs line]
  (let [cwd (:cwd fs)
        stripped (str/replace line #"^\$ " "")
        [cmd arg] (str/split stripped #" ")]
    (case cmd
      "cd"  (assoc fs :cwd (cd cwd arg))
      "ls"  fs
      "dir" (update-in fs [cwd :dirs] conj arg)
      (update-in fs [cwd :files-size] (fnil + 0) (s/int cmd)))))

(defn- parse-output [input]
  (dissoc (reduce process-line {} (s/lines input)) :cwd))

(defn- dir-sizes [fs]
  (letfn-mem
   [(size-of [path]
             (let [dir (fs path)]
               (+ (:files-size dir 0)
                  (sum (map #(size-of (path-for path %))
                            (:dirs dir))))))]
   (map size-of (keys fs))))

(defn part1 [input]
  (->> (parse-output input)
       dir-sizes
       (filter #(<= % 100000))
       sum))

(defn part2 [input]
  (let [sizes (dir-sizes (parse-output input))
        root-size (apply max sizes)
        needed-space (- root-size (- 70000000 30000000))]
    (->> sizes
         (filter #(>= % needed-space))
         (apply min))))
