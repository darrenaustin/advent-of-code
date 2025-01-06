;; https://adventofcode.com/2018/day/9
(ns aoc2018.day09
  (:require [aoc.day :as d]
            [aoc.util.string :as s]))

(defn input [] (d/day-input 2018 9))

;; Tried using native clojure vectors for the circular list,
;; but it was way too slow. Switched to deftype for a mutable
;; circular list.

;; TODO: is there a more succinct way to define both the protocol and
;; the implementation in the same def?

(defprotocol NodeProtocol
  (value [this])
  (right [this])
  (left [this])
  (set-left [this left-node])
  (set-right [this right-node]))

#_{:splint/disable [style lint/prefer-method-values]}
(deftype Node [value ^:unsynchronized-mutable left ^:unsynchronized-mutable right]
  NodeProtocol
  (value [this] (.-value this))
  (right [this] (.-right this))
  (left [this] (.-left this))
  (set-left [_ left-node] (set! left left-node))
  (set-right [_ right-node] (set! right right-node)))

(defprotocol CircularListProtocol
  (current [this])
  (move-left [this] [this n])
  (move-right [this] [this n])
  (insert-after [this value])
  (remove-current [this]))

#_{:splint/disable [style lint/prefer-method-values]}
(deftype CircularList [^:unsynchronized-mutable ^Node current]
  CircularListProtocol
  (current [this] (.-current this))
  (move-left [this] (set! current (left current)) this)
  (move-left [this n] (set! current (nth (iterate Node/.left current) n)) this)
  (move-right [this] (set! current (right current)) this)
  (move-right [this n] (set! current (nth (iterate Node/.right current) n)) this)
  (insert-after [this value]
    (let [node (->Node value current (right current))]
      (set-right (left node) node)
      (set-left (right node) node)
      (set! current node)
      this))
  (remove-current [_]
    (let [left (left current) value (value current) right (right current)]
      (set-right left right)
      (set-left right left)
      (set! current right)
      value)))

(defn circular [value]
  (let [node (->Node value nil nil)]
    (-> node
        (set-left node)
        (set-right node)
        ->CircularList)))

(defn play-game [players last-marble]
  (let [circle (circular 0)]
    (loop [marble 1 player 0 scores {}]
      (if (> marble last-marble)
        (apply max (vals scores))
        (if (zero? (mod marble 23))
          (let [removed (-> circle (move-left 7) remove-current)]
            (recur (inc marble)
                   (mod (inc player) players)
                   (assoc scores player (+ (get scores player 0) marble removed))))
          (do
            (-> circle move-right (insert-after marble))
            (recur (inc marble) (mod (inc player) players) scores)))))))

(defn part1 [input]
  (let [[players last-marble] (s/parse-ints input)]
    (play-game players last-marble)))

(defn part2 [input]
  (let [[players last-marble] (s/parse-ints input)]
    (play-game players (* 100 last-marble))))
