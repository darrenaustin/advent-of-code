(ns aoc2016.assembunny
  (:require
   [aoc.util.string :as s]
   [clojure.string :as str]))

(defn- parse-instruction [line]
  (let [[op x y] (str/split line #" ")]
    [(keyword op)
     (or (s/int x) (keyword x))
     (or (s/int y) (keyword y))]))

(defn init-program
  ([input] (init-program input {}))
  ([input registers]
   {:pc 0
    :code (mapv parse-instruction (s/lines input))
    :regs (merge {:a 0, :b 0, :c 0, :d 0} registers)
    :output []
    :done false}))

(defn- toggle [pc program]
  (if-let [[op x y] (get program pc)]
    (assoc program pc
           (case op
             :cpy [:jnz x y]
             :inc [:dec x y]
             :dec [:inc x y]
             :jnz [:cpy x y]
             :tgl [:inc x y]
             :out [:inc x y]))
    program))

(defn- multiplication [pc program]
  ;; Look for something like:
  ;;
  ;; 0: cpy <value> b
  ;; 1: inc a
  ;; 2: dec b
  ;; 3: jnz b -2
  ;; 4: dec c
  ;; 5: jnz c -5
  ;;
  ;; Which will be a loop resulting in:
  ;; a += <value> * c, and b, c = 0.
  (let [block-end (+ pc 6)]
    (when (<= block-end (count program))
      (let [block (subvec program pc block-end)]
        (when (and (= (mapv first block) [:cpy :inc :dec :jnz :dec :jnz])
                   (= -2 (get-in block [3 2]))
                   (= -5 (get-in block [5 2])))
          (let [m (get-in block [0 1])
                a (get-in block [1 1])
                b (get-in block [0 2])
                c (get-in block [4 1])]
            (when (and (= b (get-in block [2 1]) (get-in block [3 1]))
                       (= c (get-in block [4 1]) (get-in block [5 1])))
              [m a b c])))))))

(defn- addition [pc program]
  ;; inc x; dec y; jnz y -2
  ;; OR
  ;; dec y; inc x; jnz y -2
  (let [block-end (+ pc 3)]
    (when (<= block-end (count program))
      (let [block (subvec program pc block-end)]
        (cond
          (and (= (mapv first block) [:inc :dec :jnz])
               (= -2 (get-in block [2 2]))
               (= (get-in block [1 1]) (get-in block [2 1])))
          [(get-in block [0 1]) (get-in block [1 1])]

          (and (= (mapv first block) [:dec :inc :jnz])
               (= -2 (get-in block [2 2]))
               (= (get-in block [0 1]) (get-in block [2 1])))
          [(get-in block [1 1]) (get-in block [0 1])])))))

(defn- division [pc program]
  ;; 0: cpy 2 c
  ;; 1: jnz b 2
  ;; 2: jnz 1 6
  ;; 3: dec b
  ;; 4: dec c
  ;; 5: jnz c -4
  ;; 6: inc a
  ;; 7: jnz 1 -7
  (let [block-end (+ pc 8)]
    (when (<= block-end (count program))
      (let [block (subvec program pc block-end)]
        (when (and (= (mapv first block) [:cpy :jnz :jnz :dec :dec :jnz :inc :jnz])
                   (= 2 (get-in block [0 1]))
                   (= 2 (get-in block [1 2]))
                   (= 6 (get-in block [2 2]))
                   (= -4 (get-in block [5 2]))
                   (= -7 (get-in block [7 2])))
          (let [c (get-in block [0 2])
                b (get-in block [1 1])
                a (get-in block [6 1])]
            (when (and (= b (get-in block [3 1]))
                       (= c (get-in block [4 1]) (get-in block [5 1])))
              [a b c])))))))

(defn- value [program x]
  (get (:regs program) x x))

(defn step [program]
  (let [{:keys [pc code regs]} program]
    (if (>= pc (count code))
      (assoc program :done true)
      (if-let [[a b c] (division pc code)]
        (if (pos? (value program b))
          (-> program
              (update :pc + 8)
              (assoc :regs (-> regs
                               (update a + (quot (value program b) 2))
                               (assoc c (- 2 (mod (value program b) 2)))
                               (assoc b 0))))
          (update program :pc inc))
        (if-let [[m a b c] (multiplication pc code)]
          (if (and (pos? (value program c)) (pos? (value program m)))
            (-> program
                (update :pc + 6)
                (assoc :regs (-> regs
                                 (update a + (* (value program m) (value program c)))
                                 (assoc b 0)
                                 (assoc c 0))))
            (update program :pc inc))
          (if-let [[x y] (addition pc code)]
            (if (pos? (value program y))
              (-> program
                  (update :pc + 3)
                  (assoc :regs (-> regs
                                   (update x + (value program y))
                                   (assoc y 0))))
              (update program :pc inc))
            (let [[op x y] (code pc)]
              (case op
                :cpy (if (keyword? y)
                       (-> program
                           (update :pc inc)
                           (assoc-in [:regs y] (value program x)))
                       (update program :pc inc))
                :inc (if (keyword? x)
                       (-> program
                           (update :pc inc)
                           (update-in [:regs x] inc))
                       (update program :pc inc))
                :dec (if (keyword? x)
                       (-> program
                           (update :pc inc)
                           (update-in [:regs x] dec))
                       (update program :pc inc))
                :jnz (update program :pc (if (zero? (value program x))
                                           inc
                                           #(+ (value program y) %)))
                :tgl (-> program
                         (update :pc inc)
                         (assoc :code (toggle (+ pc (value program x)) code)))
                :out (-> program
                         (update :pc inc)
                         (update :output conj (value program x)))))))))))

(defn execute [program]
  (loop [prog program]
    (if (:done prog)
      prog
      (recur (step prog)))))

(defn assembunny
  ([input] (assembunny input {}))
  ([input registers]
   (:regs (execute (init-program input registers)))))
