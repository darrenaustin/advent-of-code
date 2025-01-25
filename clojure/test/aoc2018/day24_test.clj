;; Test for aoc2018.day24
(ns aoc2018.day24-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2018.day24 :as d]
            [clojure.test :refer :all]))

(def example
  "Immune System:
17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2
989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3

Infection:
801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1
4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4")

(deftest part1-example
  (is (= 5216 (d/part1 example))))

(deftest part2-example
  (is (= 51 (d/part2 example))))

(deftest correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2018 24) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
