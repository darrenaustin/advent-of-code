(ns aoc.util.bounded)

(defprotocol Bounded
  "Protocol for 2D spatial bounds and dimensions."
  (bounds [this] "Returns a pair of [[min-x min-y] [max-x max-y]] coordinates.")
  (width [this] "Returns the width (number of columns) of the grid.")
  (height [this] "Returns the height (number of rows) of the grid."))

(defn size
  "Returns the [width height] of the grid."
  [grid] [(width grid) (height grid)])

(defn top-left "Returns the [x y] coordinate of the top-left corner."
  [grid] (first (bounds grid)))

(defn top-right "Returns the [x y] coordinate of the top-right corner."
  [grid]
  (let [[[_ min-y] [max-x _]] (bounds grid)]
    [max-x min-y]))

(defn bottom-right "Returns the [x y] coordinate of the bottom-right corner."
  [grid] (second (bounds grid)))

(defn bottom-left "Returns the [x y] coordinate of the bottom-left corner."
  [grid]
  (let [[[min-x _] [_ max-y]] (bounds grid)]
    [min-x max-y]))

(defn corners "Returns a sequence of the four corner coordinates."
  [grid]
  (let [[[min-x min-y] [max-x max-y]] (bounds grid)]
    [[min-x min-y] [max-x min-y] [max-x max-y] [min-x max-y]]))
