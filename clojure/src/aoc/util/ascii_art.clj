(ns aoc.util.ascii-art
  (:require
   [aoc.util.collection :as c]
   [clojure.string :as str]))

(defn- blank-row? [row]
  (every? #{\space} row))

(defn- trim-vertical [lines]
  (->> lines
       (drop-while blank-row?)
       (take-while (complement blank-row?))))

(defn- word->glyphs [word]
  (let [height (count word)
        spaces (repeat height \space)]
    (->> word
         c/transpose
         (c/split #{spaces})
         (map (partial remove #{spaces}))
         (map c/transpose)
         (mapv (partial map str/join)))))

(defn- make-font [alphabet ascii-alphabet]
  (let [characters (word->glyphs ascii-alphabet)]
    (into {} (map vector characters alphabet))))

;; Snagged the ascii alphabets from:
;; https://github.com/mstksg/advent-of-code-ocr/blob/main/src/Advent/OCR/LetterMap.hs

(def fonts
  {6 (make-font "ABCEFGHIJKLOPRSUYZ"
                [" ##  ###   ##  #### ####  ##  #  # ###   ## #  # #     ##  ###  ###   ### #  # #   # ####"
                 "#  # #  # #  # #    #    #  # #  #  #     # # #  #    #  # #  # #  # #    #  # #   #    #"
                 "#  # ###  #    ###  ###  #    ####  #     # ##   #    #  # #  # #  # #    #  #  # #    # "
                 "#### #  # #    #    #    # ## #  #  #     # # #  #    #  # ###  ###   ##  #  #   #    #  "
                 "#  # #  # #  # #    #    #  # #  #  #  #  # # #  #    #  # #    # #     # #  #   #   #   "
                 "#  # ###   ##  #### #     ### #  # ###  ##  #  # ####  ##  #    #  # ###   ##    #   ####"])

   10 (make-font "ABCEFGHJKLNPRXZ"
                 ["  ##   #####   ####  ###### ######  ####  #    #    ### #    # #      #    # #####  #####  #    # ######"
                  " #  #  #    # #    # #      #      #    # #    #     #  #   #  #      ##   # #    # #    # #    #      #"
                  "#    # #    # #      #      #      #      #    #     #  #  #   #      ##   # #    # #    #  #  #       #"
                  "#    # #    # #      #      #      #      #    #     #  # #    #      # #  # #    # #    #  #  #      # "
                  "#    # #####  #      #####  #####  #      ######     #  ##     #      # #  # #####  #####    ##      #  "
                  "###### #    # #      #      #      #  ### #    #     #  ##     #      #  # # #      #  #     ##     #   "
                  "#    # #    # #      #      #      #    # #    #     #  # #    #      #  # # #      #   #   #  #   #    "
                  "#    # #    # #      #      #      #    # #    # #   #  #  #   #      #   ## #      #   #   #  #  #     "
                  "#    # #    # #    # #      #      #   ## #    # #   #  #   #  #      #   ## #      #    # #    # #     "
                  "#    # #####   ####  ###### #       ### # #    #  ###   #    # ###### #    # #      #    # #    # ######"])})

(defn ocr
  "Parses ASCII art text into a string.
   Input should be a sequence of strings representing the rows of the image.
   Handles standard 6-high and 10-high fonts used in Advent of Code.
   Returns '?' for unrecognized characters.

   Options:
   - `:on`: Character representing an 'on' pixel (default: \\#).
   - Any other character is considered an 'off' pixel."
  [ascii-word & {:keys [on] :or {on \#}}]
  (let [normalized (map (fn [row] (str/join (map #(if (= % on) \# \space) row))) ascii-word)
        trimmed (trim-vertical normalized)
        height (count trimmed)
        glyphs (word->glyphs trimmed)]
    (str/join (map #(get-in fonts [height %] "?") glyphs))))
