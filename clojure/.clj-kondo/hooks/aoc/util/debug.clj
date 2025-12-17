(ns hooks.aoc.util.debug
  (:require
   [clj-kondo.hooks-api :as api]))

#_{:clojure-lsp/ignore [:clojure-lsp/unused-public-var]}
(defn dbg-> [{:keys [node]}]
  (let [[x & args] (rest (:children node))]
    (if (empty? args)
      {:node x}
      (let [arg1 (first args)]
        (if (string? (api/sexpr arg1))
          (if (= 1 (count args))
            {:node x}
            (let [form (second args)]
              {:node (api/list-node
                      (list (api/token-node '->)
                            x
                            form))}))
          {:node (api/list-node
                  (list (api/token-node '->)
                        x
                        arg1))})))))

#_{:clojure-lsp/ignore [:clojure-lsp/unused-public-var]}
(defn dbg->> [{:keys [node]}]
  (let [all-args (rest (:children node))
        x (last all-args)
        args (butlast all-args)]
    (if (empty? args)
      {:node x}
      (let [arg1 (first args)]
        (if (string? (api/sexpr arg1))
          (if (= 1 (count args))
            {:node x}
            (let [form (second args)]
              {:node (api/list-node
                      (list (api/token-node '->>)
                            x
                            form))}))
          {:node (api/list-node
                  (list (api/token-node '->>)
                        x
                        arg1))})))))
