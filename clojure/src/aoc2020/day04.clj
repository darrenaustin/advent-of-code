;; https://adventofcode.com/2020/day/4
 (ns aoc2020.day04
   (:require
    [aoc.day :as d]
    [aoc.util.collection :refer [count-where]]
    [aoc.util.string :as s]
    [clojure.set :as set]
    [clojure.string :as str]))

(defn input [] (d/day-input 2020 4))

(def ^:private field-validators
  {"byr" #(<= 1920 (s/int %) 2002)
   "iyr" #(<= 2010 (s/int %) 2020)
   "eyr" #(<= 2020 (s/int %) 2030)
   "hgt" #(or (and (str/includes? % "cm")
                   (<= 150 (s/int %) 193))
              (and (str/includes? % "in")
                   (<= 59 (s/int %) 76)))
   "hcl" #(re-matches #"^#[0-9a-f]{6}$" %)
   "ecl" #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"}
   "pid" #(re-matches #"^[0-9]{9}$" %)})

(def ^:private required-fields (set (keys field-validators)))

(defn- parse-passport [block]
  (into {} (map (comp vec rest) (re-seq #"(\w+):(\S+)" block))))

(defn- required-fields? [passport]
  (= (set/intersection required-fields (set (keys passport)))
     required-fields))

(defn- valid-required-fields? [passport]
  (every? (fn [[field valid?]]
            (when-let [val (passport field)]
              (valid? val)))
          field-validators))

(defn- valid-passports [input validator]
  (count-where validator (map parse-passport (s/blocks input))))

(defn part1 [input] (valid-passports input required-fields?))

(defn part2 [input] (valid-passports input valid-required-fields?))
