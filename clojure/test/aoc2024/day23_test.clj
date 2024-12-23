;; Test for aoc2024.day23
(ns aoc2024.day23-test
  (:require [aoc.day :refer [day-answers]]
            [aoc2024.day23 :as d]
            [clojure.test :refer :all]))

(def example
  "kh-tc
qp-kh
de-cg
ka-co
yn-aq
qp-ub
cg-tb
vc-aq
tb-ka
wh-tc
yn-cg
kh-ub
ta-co
de-co
tc-td
tb-wq
wh-td
ta-ka
td-qp
aq-cg
wq-ub
ub-vc
de-ta
wq-aq
wq-vc
wh-yn
ka-de
kh-ta
co-tc
wh-qp
tb-vc
td-yn")

(deftest part1-example
  (is (= 7 (d/part1 example))))

(deftest part2-example
  (is (= "co,de,ka,ta" (d/part2 example))))

(deftest ^:slow correct-answers
  (let [{:keys [answer1 answer2]} (day-answers 2024 23) input (d/input)]
    (is (= answer1 (d/part1 input)))
    (is (= answer2 (d/part2 input)))))
