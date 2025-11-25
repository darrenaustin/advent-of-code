;; Test for aoc2020.day21
(ns aoc2020.day21-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2020.day21 :as d]
            [clojure.test :refer :all]))

(def example
  "mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
trh fvjkl sbzzf mxmxvkd (contains dairy)
sqjhc fvjkl (contains soy)
sqjhc mxmxvkd sbzzf (contains fish)")


(deftest part1-example
  (is (= 5 (d/part1 example))))

(deftest part2-example
  (is (= "mxmxvkd,sqjhc,fvjkl" (d/part2 example))))

(def answers (delay (day-answers 2020 21)))
(def input (delay (d/input)))

(deftest part1-correct
  (is (= (:answer1 @answers) (d/part1 @input))))

(deftest part2-correct
  (is (= (:answer2 @answers) (d/part2 @input))))
