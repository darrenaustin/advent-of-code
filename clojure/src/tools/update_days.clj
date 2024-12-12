(ns tools.update-days
  (:require
   [clojure.java.io :as io])
  (:gen-class))

(defn find-days []
  (->> (file-seq (io/file "src"))
       (map #(re-matches #"src/aoc(\d\d\d\d)/day(\d\d).clj" (.getPath %)))
       (filter seq)
       sort
       (map (fn [[_ year day-num]]
              [(Integer/parseInt year) (Integer/parseInt day-num)]))))

(defn day-namespace [[year day-num]]
  (format "aoc%04d.day%02d" year day-num))

(defn update-days-file []
  (let [days-file (io/file "src/aoc/days.clj")
        days (find-days)]
    (with-open [wrtr (io/writer days-file)]
      (.write wrtr ";; Machine generated. Do not edit by hand.\n")
      (.write wrtr ";;\n")
      (.write wrtr ";; To regenerate this file:\n")
      (.write wrtr ";;   clj -M tools.update-days\n")
      (.write wrtr "\n")
      (.write wrtr "(ns aoc.days\n")
      (.write wrtr "  (:require\n")
      (doseq [day (butlast days)]
        (.write wrtr (format "   [%s]\n" (day-namespace day))))
      (.write wrtr (format "   [%s]))\n" (day-namespace (last days)))))))

(defn -main [& _] (update-days-file))
