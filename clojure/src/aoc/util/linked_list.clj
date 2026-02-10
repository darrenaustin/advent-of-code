(ns aoc.util.linked-list
  "A mutable doubly-linked list implementation, primarily designed for
   performance-critical circular buffer manipulations.
   The core type `ListNode` is mutable."
  (:refer-clojure :exclude [format next remove]))

(defprotocol DoublyLinkedNode
  "Protocol for a mutable doubly-linked list node."
  (value [this])
  (previous [this])
  (set-previous! [this node])
  (next [this])
  (set-next! [this node]))

(deftype ListNode [val ^:volatile-mutable prv ^:volatile-mutable nxt]
  DoublyLinkedNode
  (value [_] val)
  (previous [_] prv)
  (set-previous! [this node] (set! prv node) this)
  (next [_] nxt)
  (set-next! [this node] (set! nxt node) this)

  Object
  (toString [_] (str val)))

(defn insert-after!
  "Inserts `node` immediately after the `left` node in the list.
   Returns the udpated `node`."
  [left node]
  (let [right (next left)]
    (set-next! left node)
    (set-previous! right node)
    (set-previous! node left)
    (set-next! node right)))

(defn insert-before!
  "Inserts `node` immediately before the `right` node in the list.
   Returns the udpated `node`."
  [right node]
  (insert-after! (previous right) node))

(defn remove!
  "Removes `node` from the list by linking its previous and next nodes together.
   Returns the updated `node` with it's next and previous set to nil."
  [node]
  (let [left (previous node)
        right (next node)]
    (set-previous! right left)
    (set-next! left right)
    (set-previous! node nil)
    (set-next! node nil)))

(defn shift-left!
  "Moves `node` `n` positions to the left (backwards) in the list.
   Returns `node`."
  [node n]
  (if (zero? n)
    node
    (let [after (nth (iterate previous node) (inc n))]
      (insert-after! after (remove! node)))))

(defn shift-right!
  "Moves `node` `n` positions to the right (forwards) in the list.
   Returns `node`."
  [node n]
  (if (zero? n)
    node
    (let [after (nth (iterate next node) n)]
      (insert-after! after (remove! node)))))

(defn make-circular-node
  "Creates a single node containing `value` linked to itself, forming a circular list of size 1.
   Returns the new `ListNode`."
  [value]
  (let [node (ListNode. value nil nil)]
    (set-previous! node node)
    (set-next! node node)))

(defn make-circular-list
  "Creates a circular doubly-linked list from the collection `coll`.
   Returns a vector of `ListNode`s in the same order as `coll`.
   The list structure is mutable and established via the nodes' pointers."
  [coll]
  (reduce (fn [nodes v]
            (->> (ListNode. v nil nil)
                 (insert-after! (peek nodes))
                 (conj nodes)))
          (vector (make-circular-node (first coll)))
          (rest coll)))

(defn format
  "Returns a string representation of the circular list starting at `node`.
   Traverses the list until it returns to `node`.
   Returns a format like `(val1 val2 ...)`."
  [node]
  (loop [current (next node), values [(value node)]]
    (if (identical? current node)
      (str (seq values))
      (recur (next current) (conj values (value current))))))
