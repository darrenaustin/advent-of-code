;; https://adventofcode.com/2024/day/9
(ns aoc2024.day09
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.math :as m]))

(def input (d/day-input 2024 9))

(defn expand-file-map [input]
  (vec (apply concat (map-indexed
                      (fn [i [f e]]
                        (concat (repeat f i) (when e (repeat e nil))))
                      (partition-all 2 (map #(read-string (str %)) input))))))

(defn defrag-blocks [file-map]
  (let [open-indices (filter identity (map-indexed (fn [i e] (when (nil? e) i)) file-map))
        value-indices (reverse (filter identity (map-indexed (fn [i e] (when e i)) file-map)))]
    (reduce (fn [fm [oi vi]]
              ;; (println "Swaping " oi "(" (fm oi) ") with" vi "(" (fm vi) ")")
              (if (< vi (count value-indices))
                (reduced fm)
                (assoc fm oi (fm vi) vi nil)))
            file-map
            (map vector open-indices value-indices))))

(defn find-moveable-file [indexed-blocks]
  (loop [files (reverse (filter (fn [[_ [v _]]] v) indexed-blocks))]
    (when-let [[file-idx [_ file-sz]] (first files)]
      (if-let [[open-idx _] (first (filter (fn [[_ [v open-sz]]] (and (nil? v) (<= file-sz open-sz))) indexed-blocks))]
        (if (> file-idx open-idx)
          [file-idx open-idx]
          (recur (rest files)))
        (recur (rest files))))))

(defn coll-insert [coll idx value]
  (let [[before after] (split-at idx coll)]
    (vec (concat before [value] after))))

(defn move-file [block-groups file-idx open-idx]
  (let [[file-val file-sz] (get block-groups file-idx)
        [_ space-sz] (get block-groups open-idx)
        extra-space (- space-sz file-sz)
        file-moved-groups (assoc block-groups
                                 file-idx [nil file-sz]
                                 open-idx [file-val file-sz])]
    (if (zero? extra-space)
      file-moved-groups
      (coll-insert file-moved-groups (inc open-idx) [nil extra-space]))))

(defn next-index
  ([coll pred] (next-index coll pred -1))
  ([coll pred from-index]
   (first (filter #(pred (get coll %)) (range (inc from-index) (count coll))))))

(defn prev-index
  ([coll pred] (prev-index coll pred (count coll)))
  ([coll pred from-index]
   (first (filter #(pred (get coll %)) (range (dec from-index) 0 -1)))))

(defn defrag-files [file-map]
  (loop [block-groups (mapv (fn [g] [(first g) (count g)])
                            (partition-by identity file-map))]
    (let [indexed-groups (c/indexed block-groups)
          [file-idx open-idx] (find-moveable-file indexed-groups)]
      (if open-idx
        (recur (move-file block-groups file-idx open-idx))
        (vec (apply concat (map (fn [[v sz]] (repeat sz v))
                                block-groups)))))))

(defn checksum [file-map]
 (m/sum (map-indexed (fn [i e] (if e (* i e) 0)) file-map)))

;; TODO: clean up and speed up!
(defn part1 [input]
  (let [expanded (expand-file-map input)]
    (checksum (defrag-blocks expanded))))

(defn part2 [input]
  (let [expanded (expand-file-map input)]
    (checksum (defrag-files expanded))))
