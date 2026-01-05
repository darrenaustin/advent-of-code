(ns kaocha-plugins.timing
  (:require
   [kaocha.plugin :as p]))

(def ^:dynamic *threshold-ms* 1000)

#_{:splint/disable [style lint/prefer-method-values]}
(p/defplugin kaocha-plugins.timing/report-slow
  (pre-test [test-able test-plan]
            (assoc test-able ::start (System/nanoTime)))

  (post-test [test-able test-plan]
             (let [start (::start test-able)
                   ms (/ (- (System/nanoTime) start) 1e6)]
               (when (and (> ms *threshold-ms*)
                          (= :kaocha.type/var (:kaocha.testable/type test-able)))
                 ;; Use the file descriptor to get to stderr and around the kaocha output capture shenanigans
                 (let [fos (java.io.FileOutputStream. java.io.FileDescriptor/err)]
                   (.write fos (.getBytes (str "\nSLOW TEST " (:kaocha.testable/id test-able) (format " %.1fms" ms) "\n")))
                   (.flush fos))))
             test-able))
