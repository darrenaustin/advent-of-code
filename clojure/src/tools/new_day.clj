(ns tools.new-day
  (:require [aoc.day :as d]
            [clojure.java.io :as io]
            [tools.update-days :as upd])
  (:gen-class))

(defn create-src-year-dir [year]
  (let [dir (io/file (format "src/aoc%04d" year))]
    (when (not (.isDirectory dir))
      (.mkdirs dir))))

(defn create-day-src-file [year day-num]
  (create-src-year-dir year)
  ;; TODO: looking to a templating solution for this and the other generated files.
  (let [file (io/file (format "src/aoc%04d/day%02d.clj" year day-num))]
    (when (not (.exists file))
      (with-open [wrtr (io/writer file)]
        (.write wrtr (format ";; https://adventofcode.com/%04d/day/%d\n" year day-num))
        (.write wrtr (format "(ns aoc%04d.day%02d\n" year day-num))
        (.write wrtr "  (:require\n")
        (.write wrtr "   [aoc.day :as d]\n")
        (.write wrtr "   [aoc.util.math :as m]\n")
        (.write wrtr "   [aoc.util.string :as s]))\n")
        (.write wrtr "\n")
        (.write wrtr (format "(def input (d/day-input %d %d))\n" year day-num))
        (.write wrtr "\n")
        (.write wrtr "(defn part1 [input])\n")
        (.write wrtr "\n")
        (.write wrtr "(defn part2 [input])\n")))))

(defn create-test-year-dir [year]
  (let [dir (io/file (format "test/aoc%04d" year))]
    (when (not (.isDirectory dir))
      (.mkdirs dir))))

(defn create-day-test-file [year day-num]
  (create-test-year-dir year)
  (let [file (io/file (format "test/aoc%04d/day%02d_test.clj" year day-num))]
    (when (not (.exists file))
      (with-open [wrtr (io/writer file)]
        (.write wrtr (format "(ns aoc%04d.day%02d-test\n" year day-num))
        (.write wrtr "  (:require [aoc.day :refer [day-answers]]\n")
        (.write wrtr (format "            [aoc%04d.day%02d :as d]\n" year day-num))
        (.write wrtr "            [clojure.test :refer :all]))\n")
        (.write wrtr "\n")
        (.write wrtr "(def example-input\n")
        (.write wrtr "  \"\")\n")
        (.write wrtr "\n")
        (.write wrtr "(deftest part1-example\n")
        (.write wrtr "  (is (= nil (d/part1 example-input))))\n")
        (.write wrtr "\n")
        (.write wrtr "(deftest part1-examples\n")
        (.write wrtr "  (are [expected input] (= expected (d/part1 input))\n")
        (.write wrtr "    nil \"\"))\n")
        (.write wrtr "\n")
        (.write wrtr "(deftest part2-example\n")
        (.write wrtr "  (is (= nil (d/part2 example-input))))\n")
        (.write wrtr "\n")
        (.write wrtr "(deftest part2-examples\n")
        (.write wrtr "  (are [expected input] (= expected (d/part2 input))\n")
        (.write wrtr "    nil \"\"))\n")
        (.write wrtr "\n") (.write wrtr "(deftest correct-answers\n")
        (.write wrtr (format "  (let [{:keys [answer1 answer2]} (day-answers %d %d)]\n" year day-num))
        (.write wrtr "    (is (= answer1 (d/part1 d/input)))\n")
        (.write wrtr "    (is (= answer2 (d/part2 d/input)))))\n")))))

(defn create-input-files [year day-num]
  (let [dir (io/file (format "%s/%04d/" (d/input-repo-dir), year))
        input-file (io/file (format "%s/%04d/%02d_input.txt" (d/input-repo-dir) year day-num))
        answer-file (io/file (format "%s/%04d/%02d_answer.json" (d/input-repo-dir), year day-num))]
    (when (not (.exists dir))
      (.mkdirs dir))
    (when (not (.exists input-file))
      (.createNewFile input-file))
    (when (not (.exists answer-file))
      (with-open [wrtr (io/writer answer-file)]
        (.write wrtr "{}\n")))))

(defn create-new-day-files [{:keys [year day-num]}]
  (printf "Creating day %04d.%02d\n" year day-num)
  (create-day-src-file year day-num)
  (create-day-test-file year day-num)
  (create-input-files year day-num))

(defn parse-day-spec [s]
  (if-let [[_ year _ day-num] (re-find #"(\d+)(\.(\d+))?" s)]
    {:year (when year (Integer/parseInt year))
     :day-num (when day-num (Integer/parseInt day-num))}
    (throw (Exception. (format "Invalid year.day argument: %s" s)))))

(defn usage []
  (println "Usage:\n   clj -M -m tools.new-day YYYY.dd [YYYY.dd]...\n")
  (println "Which will create the necessary src and test files")
  (println "for each YYYY.dd day specification."))

(defn -main [& args]
  (if (seq args)
    (let [days (map parse-day-spec args)]
      (doseq [day days]
        (if (nil? (:day-num day))
        ;; generate the whole year
          (doseq [day-num (range 1 26)]
            (create-new-day-files (assoc day :day-num day-num)))
          (create-new-day-files day)))
      (upd/update-days-file))
    (usage)))
