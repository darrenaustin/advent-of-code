;; https://adventofcode.com/2020/day/23
(ns aoc2020.day23
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2020 23))

(defn parse-cups [input]
  (let [labels (s/digits (s/int input))
        start (first labels)
        cup-map (loop [m {} [h & rest] labels]
                  (if h
                    (if-let [n (first rest)]
                      (recur (assoc m h n) rest)
                      (recur (assoc m h start) rest))
                    m))]
    {:cup-map cup-map
     :current start
     :min-label (apply min labels)
     :max-label (apply max labels)}))

(defn parse-padded-cups [input pad-to]
  (let [{:keys [cup-map current max-label] :as cups} (parse-cups input)
        original-end (first (c/first-where (fn [[_ v]] (= v current)) cup-map))
        start (inc max-label)
        extras (map (fn [n] [n (inc n)]) (range start pad-to))
        cup-map (into cup-map (concat extras [[original-end start] [pad-to current]]))]
    (assoc cups :cup-map cup-map :max-label pad-to)))

(defn remove-run [cup-map after length]
  (loop [cup-map cup-map run [] start (cup-map after) length (dec length)]
    (let [next (cup-map start)]
      (if (zero? length)
        [(-> cup-map (dissoc start) (assoc after next)) (conj run start)]
        (recur (dissoc cup-map start) (conj run start) next (dec length))))))

(defn insert-run [cup-map after run]
  (let [end (cup-map after)]
    (loop [cup-map cup-map after after [start & rest] run]
      (if (seq rest)
        (recur (assoc cup-map after start) start rest)
        (assoc cup-map after start start end)))))

(defn move [{:keys [cup-map current min-label max-label] :as cups}]
  ;; TODO: this whole thing could probably be sped up by doing this all in this
  ;;       function using a transient map.
  (let [[cup-map removed] (remove-run cup-map current 3)
        destination (loop [d current]
                      (let [next (if (= d min-label) max-label (dec d))]
                        (if (contains? cup-map next)
                          next
                          (recur next))))
        cup-map (insert-run cup-map destination removed)
        new-current (cup-map current)]
    (assoc cups :cup-map cup-map :current new-current)))

(defn part1
  ([input] (part1 input 100))
  ([input moves]
   (let [{:keys [cup-map]} (nth (iterate move (parse-cups input)) moves)]
     (loop [result 0 next (cup-map 1)]
       (if (= next 1)
         result
         (recur (+ (* result 10) next) (cup-map next)))))))

(defn part2 [input]
  (let [{:keys [cup-map]} (nth (iterate move (parse-padded-cups input 1000000)) 10000000)
        star1 (cup-map 1)
        star2 (cup-map star1)]
    (* star1 star2)))
