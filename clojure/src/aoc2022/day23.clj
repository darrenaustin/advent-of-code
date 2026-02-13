;; https://adventofcode.com/2022/day/23
 (ns aoc2022.day23
   (:require
    [aoc.day :as d]
    [aoc.util.string :as s]
    [clojure.data.int-map :refer [dense-int-set int-map]]))

(defn input [] (d/day-input 2022 23))

(def ^:private ^:const coord-range 2048)
(def ^:private ^:const coord-zero (/ coord-range 2))
(def ^:private ^:const N (- coord-range))
(def ^:private ^:const S coord-range)
(def ^:private ^:const W -1)
(def ^:private ^:const E 1)

(def ^:private directions
  [[N (+ N W) (+ N E)]   ; N
   [S (+ S W) (+ S E)]   ; S
   [W (+ N W) (+ S W)]   ; W
   [E (+ N E) (+ S E)]]) ; E

(def ^:private adjacent-deltas
  (long-array [(+ N W) N (+ N E) W E (+ S W) S (+ S E)]))

(defn- parse-elves [input]
  (->> (s/lines input)
       (#(for [[y line] (map-indexed vector %)
               [x char] (map-indexed vector line)
               :when (= char \#)]
           (+ coord-zero x
              (* coord-range (+ coord-zero y)))))
       dense-int-set))

(defn- no-neighbors? [elves ^long elf]
  (loop [idx (dec (alength ^longs adjacent-deltas))]
    (if (neg? idx)
      true
      (if (elves (+ elf (aget ^longs adjacent-deltas idx)))
        false
        (recur (dec idx))))))

(defn- move-in-dir [elves ^long elf dir-deltas]
  (reduce
   (fn [move-pos ^long delta]
     (let [pos (+ elf delta)]
       (if (elves pos)
         (reduced nil)
         (or move-pos pos))))
   nil
   dir-deltas))

(defn- propose [elves ^long round proposals ^long elf]
  (if (no-neighbors? elves elf)
    proposals
    (loop [i 0]
      (if (< i 4)
        (let [n (bit-and (+ round i) 3)]
          (if-let [dest (move-in-dir elves elf (directions n))]
            (if (contains? proposals dest)
              (assoc! proposals dest nil)
              (assoc! proposals dest elf))
            (recur (inc i))))
        proposals))))

(defn- move [elves proposals]
  (persistent!
   (reduce-kv
    (fn [elves prop old]
      (if (some? old)
        (-> elves
            (disj! old)
            (conj! prop))
        elves))
    (transient elves)
    proposals)))

(defn- next-round [elves round]
  (let [proposals
        (persistent!
         (reduce (partial propose elves round)
                 (transient (int-map))
                 elves))]
    (when (seq proposals)
      (move elves proposals))))

(defn- area [elves]
  (let [xs (sort (map #(mod % coord-range) elves))
        ys (sort (map #(quot % coord-range) elves))]
    (* (inc (- (last xs) (first xs)))
       (inc (- (last ys) (first ys))))))

(defn part1 [input]
  (let [elves (reduce next-round (parse-elves input) (range 10))]
    (- (area elves) (count elves))))

(defn part2 [input]
  (reduce
   (fn [elves round]
     (let [new-elves (next-round elves round)]
       (if (nil? new-elves)
         (reduced (inc round))
         new-elves)))
   (parse-elves input)
   (iterate inc 0)))
