;; https://adventofcode.com/2019/day/13
 (ns aoc2019.day13
   (:require
    [aoc.day :as d]
    [aoc.util.collection :as c]
    [aoc2019.intcode :as ic]))

(defn input [] (d/day-input 2019 13))

(def ^:private block  2)
(def ^:private paddle 3)
(def ^:private ball   4)

(defn- update-state [state [x y value]]
  (cond
    (= [x y] [-1 0]) (assoc state :score value)
    (= value paddle) (assoc state :paddle [x y])
    (= value ball)   (assoc state :ball [x y])
    :else state))

(defn- joystick-input [state]
  ;; Simple strategy: move the paddle in the direction of the ball
  (let [ball-x (first (:ball state))
        paddle-x (first (:paddle state))]
    (compare ball-x paddle-x)))

(defn part1 [input]
  (->> (ic/parse-program input)
       ic/run
       :output
       (partition 3)
       (map last)
       (c/count-where #{block})))

(defn part2 [input]
  (let [game (-> (ic/parse-program input)
                 ic/init-machine
                 (assoc-in [:mem 0] 2)
                 ic/execute)]
    (loop [{:keys [status output] :as game} game, state {}]
      (if (and (= :halted status) (empty? output))
        (:score state)
        (let [state' (reduce update-state state (partition 3 output))
              joystick (joystick-input state')]
          (recur (ic/execute (ic/update-io game [joystick])) state'))))))
