(ns aoc.util.memoize)

(defmacro letfn-mem
  "Defines a local memoized function. The function can recursively call
   itself using the bound name, and those calls will be memoized.

   Usage:
   (letfn-mem [fib (fn [n]
                     (if (<= n 1)
                       n
                       (+ (fib (- n 1)) (fib (- n 2)))))]
     (fib 100))"
  [[name f-decl] & body]
  `(let [cache# (atom {})]
     (letfn [(~name [& args#]
               (if-let [e# (find @cache# args#)]
                 (val e#)
                 (let [res# (apply ~f-decl args#)]
                   (swap! cache# assoc args# res#)
                   res#)))]
       ~@body)))
