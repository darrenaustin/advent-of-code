(ns aoc.util.vec-test
  (:require
   [aoc.util.vec :as v]
   [clojure.test :refer :all]))

(deftest vec+-test
  (testing "vec+ with single vector"
    (is (= [1 2] (v/vec+ [1 2]))))

  (testing "vec+ with two vectors"
    (is (= [4 6] (v/vec+ [1 2] [3 4])))
    (is (= [0 0] (v/vec+ [1 2] [-1 -2])))
    (is (= [5 7] (v/vec+ [2 3] [3 4]))))

  (testing "vec+ with three or more vectors"
    (is (= [9 12] (v/vec+ [1 2] [3 4] [5 6])))
    (is (= [10 15] (v/vec+ [1 2] [3 4] [5 6] [1 3])))
    (is (= [0 0] (v/vec+ [1 2] [-1 -2] [0 0]))))

  (testing "vec+ with origin"
    (is (= [5 3] (v/vec+ v/origin [5 3])))
    (is (= v/origin (v/vec+ v/origin v/origin)))))

(deftest vec--test
  (testing "vec- with single vector (negation)"
    (is (= [-5 -3] (v/vec- [5 3])))
    (is (= [5 3] (v/vec- [-5 -3])))
    (is (= [0 0] (v/vec- [0 0]))))

  (testing "vec- with two vectors (subtraction)"
    (is (= [3 2] (v/vec- [5 3] [2 1])))
    (is (= [-2 -2] (v/vec- [1 2] [3 4])))
    (is (= [0 0] (v/vec- [5 5] [5 5]))))

  (testing "vec- with three or more vectors"
    (is (= [6 5] (v/vec- [10 8] [3 2] [1 1])))
    (is (= [0 0] (v/vec- [10 10] [5 5] [5 5])))
    (is (= [-3 -7] (v/vec- [1 2] [3 4] [1 5]))))

  (testing "vec- with origin"
    (is (= [-5 -3] (v/vec- v/origin [5 3])))
    (is (= [5 3] (v/vec- [5 3] v/origin)))))

(deftest vec-n*-test
  (testing "vec-n* scalar multiplication"
    (is (= [6 15] (v/vec-n* 3 [2 5])))
    (is (= [0 0] (v/vec-n* 0 [5 10])))
    (is (= [5 10] (v/vec-n* 1 [5 10])))
    (is (= [-4 -8] (v/vec-n* -2 [2 4])))
    (is (= [10 -20] (v/vec-n* 5 [2 -4]))))

  (testing "vec-n* with origin"
    (is (= v/origin (v/vec-n* 5 v/origin)))))

(deftest opposite-dir-test
  (testing "opposite-dir with cardinal directions"
    (is (= v/dir-down (v/opposite-dir v/dir-up)))
    (is (= v/dir-up (v/opposite-dir v/dir-down)))
    (is (= v/dir-left (v/opposite-dir v/dir-right)))
    (is (= v/dir-right (v/opposite-dir v/dir-left))))

  (testing "opposite-dir with diagonal directions"
    (is (= v/dir-se (v/opposite-dir v/dir-nw)))
    (is (= v/dir-sw (v/opposite-dir v/dir-ne)))
    (is (= v/dir-ne (v/opposite-dir v/dir-sw)))
    (is (= v/dir-nw (v/opposite-dir v/dir-se))))

  (testing "opposite-dir with custom vectors"
    (is (= [-1 -2] (v/opposite-dir [1 2])))
    (is (= [5 -3] (v/opposite-dir [-5 3]))))

  (testing "opposite-dir with origin"
    (is (= v/origin (v/opposite-dir v/origin)))))

(deftest adjacent-dirs-test
  (testing "adjacent-dirs contains all 8 directions"
    (is (= 8 (count v/adjacent-dirs)))
    (is (contains? (set v/adjacent-dirs) v/dir-nw))
    (is (contains? (set v/adjacent-dirs) v/dir-n))
    (is (contains? (set v/adjacent-dirs) v/dir-ne))
    (is (contains? (set v/adjacent-dirs) v/dir-w))
    (is (contains? (set v/adjacent-dirs) v/dir-e))
    (is (contains? (set v/adjacent-dirs) v/dir-sw))
    (is (contains? (set v/adjacent-dirs) v/dir-s))
    (is (contains? (set v/adjacent-dirs) v/dir-se))))

(deftest orthogonal-dirs-test
  (testing "orthogonal-dirs contains 4 cardinal directions"
    (is (= 4 (count v/orthogonal-dirs)))
    (is (= [v/dir-up v/dir-right v/dir-down v/dir-left] v/orthogonal-dirs)))

  (testing "orthogonal-dirs does not contain diagonals"
    (is (not (contains? (set v/orthogonal-dirs) v/dir-nw)))
    (is (not (contains? (set v/orthogonal-dirs) v/dir-ne)))
    (is (not (contains? (set v/orthogonal-dirs) v/dir-sw)))
    (is (not (contains? (set v/orthogonal-dirs) v/dir-se)))))

(deftest adjacent-to-test
  (testing "adjacent-to returns 8 positions"
    (let [result (v/adjacent-to [5 5])]
      (is (= 8 (count result)))))

  (testing "adjacent-to from origin"
    (let [result (set (v/adjacent-to v/origin))]
      (is (= #{[-1 -1] [0 -1] [1 -1]
               [-1 0]         [1 0]
               [-1 1]  [0 1]  [1 1]}
             result))))

  (testing "adjacent-to from arbitrary position"
    (let [result (set (v/adjacent-to [5 5]))]
      (is (= #{[4 4] [5 4] [6 4]
               [4 5]       [6 5]
               [4 6] [5 6] [6 6]}
             result))))

  (testing "adjacent-to includes all directions"
    (let [pos [10 10]
          result (set (v/adjacent-to pos))]
      (doseq [dir v/adjacent-dirs]
        (is (contains? result (v/vec+ pos dir)))))))

(deftest orthogonal-to-test
  (testing "orthogonal-to returns 4 positions"
    (let [result (v/orthogonal-to [5 5])]
      (is (= 4 (count result)))))

  (testing "orthogonal-to from origin"
    (let [result (v/orthogonal-to v/origin)]
      (is (= [[0 -1] [1 0] [0 1] [-1 0]] result))))

  (testing "orthogonal-to from arbitrary position"
    (let [result (v/orthogonal-to [5 5])]
      (is (= [[5 4] [6 5] [5 6] [4 5]] result))))

  (testing "orthogonal-to returns positions in correct order"
    (let [pos [7 8]
          result (v/orthogonal-to pos)]
      (is (= [(v/vec+ pos v/dir-up)
              (v/vec+ pos v/dir-right)
              (v/vec+ pos v/dir-down)
              (v/vec+ pos v/dir-left)]
             result))))

  (testing "orthogonal-to only includes cardinal directions"
    (let [pos [10 10]
          result (set (v/orthogonal-to pos))]
      (is (= 4 (count result)))
      (doseq [dir v/orthogonal-dirs]
        (is (contains? result (v/vec+ pos dir)))))))

(deftest ortho-turn-left-test
  (testing "ortho-turn-left rotates counter-clockwise"
    (is (= v/dir-left (v/ortho-turn-left v/dir-up)))
    (is (= v/dir-down (v/ortho-turn-left v/dir-left)))
    (is (= v/dir-right (v/ortho-turn-left v/dir-down)))
    (is (= v/dir-up (v/ortho-turn-left v/dir-right))))

  (testing "ortho-turn-left has all 4 cardinal directions"
    (is (= 4 (count v/ortho-turn-left)))
    (is (contains? v/ortho-turn-left v/dir-up))
    (is (contains? v/ortho-turn-left v/dir-right))
    (is (contains? v/ortho-turn-left v/dir-down))
    (is (contains? v/ortho-turn-left v/dir-left)))

  (testing "four left turns return to original direction"
    (is (= v/dir-up
           (-> v/dir-up
               v/ortho-turn-left
               v/ortho-turn-left
               v/ortho-turn-left
               v/ortho-turn-left)))))

(deftest ortho-turn-right-test
  (testing "ortho-turn-right rotates clockwise"
    (is (= v/dir-right (v/ortho-turn-right v/dir-up)))
    (is (= v/dir-down (v/ortho-turn-right v/dir-right)))
    (is (= v/dir-left (v/ortho-turn-right v/dir-down)))
    (is (= v/dir-up (v/ortho-turn-right v/dir-left))))

  (testing "four right turns return to original direction"
    (is (= v/dir-up
           (-> v/dir-up
               v/ortho-turn-right
               v/ortho-turn-right
               v/ortho-turn-right
               v/ortho-turn-right))))

  (testing "left and right turns are opposites"
    (is (= v/dir-up (v/ortho-turn-left (v/ortho-turn-right v/dir-up))))
    (is (= v/dir-right (v/ortho-turn-right (v/ortho-turn-left v/dir-right))))))

(deftest turn-maps-relationship-test
  (testing "turn right is opposite of turn left"
    (doseq [dir v/orthogonal-dirs]
      (is (= dir (v/ortho-turn-right (v/ortho-turn-left dir))))
      (is (= dir (v/ortho-turn-left (v/ortho-turn-right dir))))))

  (testing "two lefts equals two rights in opposite order"
    (doseq [dir v/orthogonal-dirs]
      (is (= (v/ortho-turn-left (v/ortho-turn-left dir))
             (v/ortho-turn-right (v/ortho-turn-right dir)))))))
