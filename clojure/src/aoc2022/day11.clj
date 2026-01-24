;; https://adventofcode.com/2022/day/11
(ns aoc2022.day11
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.math :as m]
   [aoc.util.string :as s]))

(defn input [] (d/day-input 2022 11))

(defn- parse-operation [line]
  (let [[_ op-sym param] (re-find #"new = old (\*|\+) (old|\d+)$" line)
        op (case op-sym "*" * "+" +)]
    (if (= param "old")
      (fn [old] (op old old))
      (let [operand (s/int param)]
        (fn [old] (op old operand))))))

(defn- parse-monkey [block]
  (let [[_ items-line op-line test-line true-line false-line] (s/lines block)
        test-div (s/int test-line)
        true-dest (s/int true-line)
        false-dest (s/int false-line)]
    {:items (apply c/queue (s/ints items-line))
     :operation (parse-operation op-line)
     :test-div test-div
     :throw-dest (fn [item] (if (m/div? item test-div) true-dest false-dest))
     :num-inspected 0}))

(defn- apply-relief-fn [monkey relief-fn]
  (assoc monkey :operation #(relief-fn ((:operation monkey) %))))

(defn- inspect-items [monkeys monkey-num]
  (loop [monkeys monkeys]
    (let [monkey (monkeys monkey-num)
          item (peek (:items monkey))]
      (if (nil? item)
        monkeys
        (let [item' ((:operation monkey) item)
              dest  ((:throw-dest monkey) item')]
          (recur (-> monkeys
                     (update-in [dest :items] conj item')
                     (update-in [monkey-num :items] pop)
                     (update-in [monkey-num :num-inspected] inc))))))))

(defn- next-round [monkeys]
  (reduce inspect-items monkeys (range (count monkeys))))

(defn- monkey-business [monkeys rounds relief-fn]
  (->> (mapv #(apply-relief-fn % relief-fn) monkeys)
       (iterate next-round)
       (c/nth>> rounds)
       (map :num-inspected)
       (sort >)
       (take 2)
       m/product))

(defn part1 [input]
  (monkey-business (mapv parse-monkey (s/blocks input)) 20 #(quot % 3)))

(defn part2 [input]
  (let [monkeys (mapv parse-monkey (s/blocks input))
        ease-mod (reduce m/lcm (map :test-div monkeys))]
    (monkey-business monkeys 10000 #(mod % ease-mod))))
