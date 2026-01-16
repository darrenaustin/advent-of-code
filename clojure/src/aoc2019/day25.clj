;; https://adventofcode.com/2019/day/25
(ns aoc2019.day25
  (:require
   [aoc.day :as d]
   [aoc.util.string :as s]
   [aoc2019.intcode :as ic]
   [clojure.string :as str]))

(defn input [] (d/day-input 2019 25))

(def ^:private enter-cockpit
  "Commands to pick up the needed items and navigate to the cockpit.
   There Santa will give you the code."
  ["south"
   "take fuel cell"
   "north"
   "west"
   "take mutex"
   "south"
   "south"
   "take coin"
   "north"
   "east"
   "take cake"
   "north"
   "west"
   "south"
   "west"])

(defn- run-commands [commands droid]
  (reduce
   (fn [droid cmd]
     (-> droid
         (ic/update-io (s/str->ascii (str cmd "\n")) nil)
         ic/execute))
   droid
   commands))

(defn part1 [input]
  (->> (ic/parse-program input)
       ic/run
       (run-commands enter-cockpit)
       :output
       s/ascii->str
       s/int))

(defn part2 [_] "🎄 Got em all! 🎉")

(defn- parse-command
  "Support for abbrevations and synonyms in commands.
   You can use \"get\" instead of \"take\". north, south, east,
   west and inv can be abbreviated \"n\", \"s\", \"e\", \"w\" and \"i\"."
  [command]
  (if (str/starts-with? command "get ")
    (str/replace-first command "get " "take ")
    (case command
      "n" "north"
      "s" "south"
      "e" "east"
      "w" "west"
      "i" "inv"
      command)))

(defn- interactive [droid]
  (println (s/ascii->str (:output droid)))
  (let [command (parse-command (read-line))]
    (if (or (= command "quit") (= :halted (:status droid)))
      droid
      (do
        (println ">" command)
        (recur (ic/execute
                (ic/update-io droid
                              (s/str->ascii (str command "\n"))
                              nil)))))))

(def ^:private get-all-items
  "Commands to pick up all takeable items and navigate to outside the cockpit.
   Useful for manually checking item combinations to get in."
  ["south"
   "take fuel cell"
   "south"
   "take manifold"
   "north"
   "north"
   "north"
   "take candy cane"
   "south"
   "west"
   "take mutex"
   "south"
   "south"
   "west"
   "south"
   "take prime number"
   "north"
   "take dehydrated water"
   "east"
   "take coin"
   "north"
   "east"
   "take cake"
   "north"
   "west"
   "south"])

;; To play the game interactively you can use:
;;
;; `clj -M -m aoc2019.day25`
;;
;; Or to put yourself outside the cockpit with
;; all takable items for testing you can use:
;;
;; `clj -M -m aoc2019.day25 all-items`
;;
(defn -main [& args]
  (let [all-items? (some #{"all-items"} args)
        droid (ic/run (ic/parse-program (input)))]
    (interactive (if all-items?
                   (run-commands get-all-items droid)
                   droid))))
