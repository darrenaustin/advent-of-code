(ns aoc2017.knot-hash
  (:require
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn- pinch-twist [xs start length]
  (let [size    (count xs)
        indices (mapv #(mod % size) (range start (+ start length)))
        swaps   (map vector indices (take (quot length 2) (reverse indices)))]
    (persistent!
     (reduce (fn [xs [k v]] (assoc! xs k (xs v) v (xs k)))
             (transient xs)
             swaps))))

(defn sparse-hash [xs lengths]
  (loop [xs xs start 0 skip 0 [l & ls] lengths]
    (if (nil? l)
      xs
      (recur (pinch-twist xs start l) (+ start l skip) (inc skip) ls))))

(def ^:private suffix [17 31 73 47 23])

(defn knot-hash [text]
  (str/join
   (map #(s/->hex % 2)
        (->> (apply concat (repeat 64 (concat (map int text) suffix)))
             (sparse-hash (vec (range 256)))
             (partition 16)
             (map #(reduce bit-xor %))))))
