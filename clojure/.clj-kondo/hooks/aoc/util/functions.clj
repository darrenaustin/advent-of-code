(ns hooks.aoc.util.functions
  (:require [clj-kondo.hooks-api :as api]))

(defn let-memoized [{:keys [node]}]
  (let [[bindings & body] (rest (:children node))
        [fn-name fn-def]  (:children bindings)]
    (if (and fn-name fn-def (api/list-node? fn-def))
      (let [fn-children (:children fn-def)
            first-child (first fn-children)]
        (if (and (api/token-node? first-child)
                 (= "fn" (:string-value first-child)))
          ;; It is a (fn ...) form.
          (let [second-child (second fn-children)]
            (if (api/vector-node? second-child)
              ;; (fn [args] ...) -> transform to (letfn [(name [args] ...)] ...)
              (let [fn-tail (rest fn-children) ;; ([args] body...)
                    letfn-binding (api/list-node (cons fn-name fn-tail))
                    letfn-vector (api/vector-node [letfn-binding])
                    new-node (api/list-node (list* (api/token-node "letfn")
                                                   letfn-vector
                                                   body))]
                {:node new-node})
              ;; (fn name [args] ...) -> leave as is, but wrap in let
              {:node (api/list-node
                      (list* (api/token-node "let")
                             bindings
                             body))}))
          ;; Not a fn form, just wrap in let
          {:node (api/list-node
                  (list* (api/token-node "let")
                         bindings
                         body))}))
      ;; Fallback
      {:node (api/list-node
              (list* (api/token-node "let")
                     bindings
                     body))})))
