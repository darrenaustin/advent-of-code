;; https://adventofcode.com/2019/day/14
(ns aoc2019.day14
  (:require
   [aoc.day :as d]
   [aoc.util.collection :as c]
   [aoc.util.math :as m]
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn input [] (d/day-input 2019 14))

(defn parse-chemical [input]
  (let [[amount name] (str/split input #" ")]
    {name (s/parse-int amount)}))

(defn parse-reaction [line]
  (let [[inputs output] (str/split line #" => ")]
    {:output (parse-chemical output)
     :inputs (reduce merge (map parse-chemical (str/split inputs #", ")))}))

(defn parse [input]
  (into {} (for [reaction (map parse-reaction (str/split-lines input))]
             [(first (keys (:output reaction))) reaction])))

(defn ore-for [reactions name amount]
  (letfn [(produce [given [name amount]]
            (let [given-amount (get given name 0)]
              (cond
                (= amount given-amount) (dissoc given name)
                (< amount given-amount) (assoc given name (- given-amount amount))
                (= name "ORE") (update given :ore-used + amount)
                :else (let [needed-amount   (- amount given-amount)
                            reaction        (reactions name)
                            reaction-amount (get-in reaction [:output name])
                            num-reaction    (m/ceil-div needed-amount reaction-amount)]
                        (reduce (fn [given [name amount]]
                                  (produce given [name (* amount num-reaction)]))
                                (assoc given name (- (* reaction-amount num-reaction) needed-amount))
                                (:inputs reaction))))))]
    (:ore-used (produce {:ore-used 0} [name amount]))))

(defn part1 [input]
  (ore-for (parse input) "FUEL" 1))

(defn part2 [input]
  (let [reactions (parse input)
        upper     (c/first-where #(< 1000000000000 (ore-for reactions "FUEL" %))
                                 (iterate #(* 2 %) 1))]
    (loop [low (quot upper 2), high upper]
      (if (< low high)
        (let [mid (quot (+ low high) 2)]
          (if (< 1000000000000 (ore-for reactions "FUEL" mid))
            (recur low mid)
            (recur (inc mid) high)))
        (dec low)))))
