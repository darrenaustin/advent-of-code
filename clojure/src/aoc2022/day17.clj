;; https://adventofcode.com/2022/day/17
 (ns aoc2022.day17
   (:require
    [aoc.day :as d]))

(defn input [] (d/day-input 2022 17))

(def ^:private tower-width 7)
(def ^:private cycle-detection-rows 6)

(def ^:private rocks
  ;; Turn the shapes into vecs of row bitmaps, bottom-up, right-left to
  ;; make positioning and updating the tower easier.
  [{:rows [2r1111]            :width 4}
   {:rows [2r010 2r111 2r010] :width 3}
   {:rows [2r111 2r100 2r100] :width 3}
   {:rows [2r1 2r1 2r1 2r1]   :width 1}
   {:rows [2r11 2r11]         :width 2}])

(defn move-left  [[x y]] [(dec x) y])
(defn move-right [[x y]] [(inc x) y])
(defn move-down  [[x y]] [x (dec y)])

(defn- invalid? [tower rock [x y]]
  (or (neg? y) (neg? x) (> (+ x (:width rock)) tower-width)
      (and (< y (count tower))
           (->> (:rows rock)
                (map-indexed #(bit-and (bit-shift-left %2 x)
                                       (nth tower (+ y %1) 0)))
                (some pos?)))))

(defn- place-rock [tower rock [x y]]
  (reduce
   (fn [tower row]
     (update tower (+ y row) #(bit-or (bit-shift-left (rock row) x) (or % 0))))
   tower
   (range (count rock))))

(defn- drop-rock [{:keys [tower jet-idx jets rock-num]}]
  (let [rock-idx (mod rock-num (count rocks))
        rock (nth rocks rock-idx)]
    (loop [jet-idx jet-idx
           pos-start [2 (+ 3 (count tower))]]
      (let [jet-idx (mod jet-idx (count jets))
            pos-pushed ((nth jets jet-idx) pos-start)
            pos (if (invalid? tower rock pos-pushed) pos-start pos-pushed)
            pos-fallen (move-down pos)]
        (if (invalid? tower rock pos-fallen)
          {:tower (place-rock tower (:rows rock) pos)
           :jet-idx (inc jet-idx)
           :jets jets
           :rock-idx (inc rock-idx)
           :rock-num (inc rock-num)}
          (recur (inc jet-idx) pos-fallen))))))

(defn- find-cycle [states]
  (reduce
   (fn [seen state]
     (let [top-rows (max 0 (- (count (:tower state)) cycle-detection-rows))
           cycle-state [(subvec (:tower state) top-rows) (:jet-idx state) (:rock-idx state)]]
       (if-let [cycle-start (get seen cycle-state)]
         (reduced [cycle-start state])
         (assoc seen cycle-state state))))
   {}
   states))

(defn- height-after [input num-rocks]
  (let [jets (mapv {\< move-left, \> move-right} input)
        start {:tower (vector-of :long), :jet-idx 0, :jets jets :rock-num 0}
        states (iterate drop-rock start)
        [start end] (find-cycle states)
        cycle-start (:rock-num start)
        cycle-height (- (count (:tower end)) (count (:tower start)))
        cycle-period (- (:rock-num end) cycle-start)
        cycles (quot (- num-rocks cycle-start) cycle-period)
        steps-left (- num-rocks (* cycles cycle-period) cycle-start)]
    (+ (count (:tower (nth states (+ cycle-start steps-left))))
       (* cycle-height cycles))))

(defn part1 [input] (height-after input 2022))

(defn part2 [input] (height-after input 1000000000000))
