(ns aoc.core
  (:require
   [aoc.day :as d]
   [aoc.days])
  (:gen-class))

(defn all-days []
  (sort
   (transduce
    (comp (map ns-name)
          (map name)
          (map #(re-find #"aoc(\d+).day(\d+)$" %))
          (remove nil?)
          (map (fn [[_ ys ds]] [(Integer/parseInt ys) (Integer/parseInt ds)])))
    conj
    (all-ns))))

(defn matching-day? [[year day-num] specs]
  (some
   (fn [[spec-year spec-day-num]]
     (and (= year spec-year)
          (or (= day-num spec-day-num)
              (nil? spec-day-num))))
   specs))

(defn specified-days [specs days]
  (if (seq specs)
    (filter #(matching-day? % specs) days)
    days))

(defn parse-day-specs [s]
  (if-let [[_ year _ day-num] (re-find #"(\d+)(\.(\d+))?" s)]
    [(when year (read-string year))
     (when day-num (read-string day-num))]
    (throw (Exception. (format "Invalid year.day argument: %s" s)))))

(defn -main [& args]
  (let [day-specs (map parse-day-specs args)]
    (doseq [[year day] (specified-days day-specs (all-days))]
      (d/execute year day))))
