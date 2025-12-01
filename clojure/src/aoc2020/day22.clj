;; https://adventofcode.com/2020/day/22
(ns aoc2020.day22
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2020 22))

(defn parse-decks [input]
  (let [[player1 player2] (s/split-blocks input)]
    [(rest (s/parse-ints player1))
     (rest (s/parse-ints player2))]))

(defn play-round [[deck1 deck2]]
  (let [[card1 & deck1] deck1
        [card2 & deck2] deck2]
    (cond
      (> card1 card2) [(concat deck1 [card1 card2]) deck2]
      (< card1 card2) [deck1 (concat deck2 [card2 card1])])))

(defn play-game [decks]
  (loop [[deck1 deck2] decks]
    (cond
      (empty? deck1) deck2
      (empty? deck2) deck1
      :else (recur (play-round [deck1 deck2])))))

(defn score-deck [deck]
  (reduce + (map * (range (count deck) 0 -1) deck)))

(defn part1 [input]
  (-> input
      parse-decks
      play-game
      score-deck))

(declare play-recursive-game)

(defn play-recursive-round [[deck1 deck2]]
  (let [[card1 & deck1] deck1
        [card2 & deck2] deck2]
    (cond
      (and (>= (count deck1) card1)
           (>= (count deck2) card2))
      (let [sub-deck1 (take card1 deck1)
            sub-deck2 (take card2 deck2)
            [winner _] (play-recursive-game [sub-deck1 sub-deck2])]
        (case winner
          :first  [(concat deck1 [card1 card2]) deck2]
          :second [deck1 (concat deck2 [card2 card1])]))

      (> card1 card2) [(concat deck1 [card1 card2]) deck2]
      (< card1 card2) [deck1 (concat deck2 [card2 card1])])))

(defn play-recursive-game [decks]
  ;; (println "Starting game with decks:" decks)
  (loop [[deck1 deck2] decks rounds #{}]
    ;; (println " Round:" [deck1 deck2] " Previous rounds:" rounds)
    (cond
      (rounds [deck1 deck2]) [:first deck1]
      (empty? deck1) [:second deck2]
      (empty? deck2) [:first  deck1]
      :else
      (let [rounds (conj rounds [deck1 deck2])
            [deck1 deck2] (play-recursive-round [deck1 deck2])]
        (recur [deck1 deck2] rounds)))))

;; TODO: this is too slow
(defn part2 [input]
  (-> input
      parse-decks
      play-recursive-game
      second
      score-deck))
