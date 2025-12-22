(ns aoc.util.ascii-art-test
  (:require
   [aoc.util.ascii-art :as ascii-art]
   [clojure.string :as str]
   [clojure.test :refer [deftest is testing]]))

(def letter-a-6
  [" ##  "
   "#  # "
   "#  # "
   "#### "
   "#  # "
   "#  # "])

(def letter-b-6
  ["###  "
   "#  # "
   "###  "
   "#  # "
   "#  # "
   "###  "])

(def beef-6
  ["###  #### #### ####"
   "#  # #    #    #   "
   "###  ###  ###  ### "
   "#  # #    #    #   "
   "#  # #    #    #   "
   "###  #### #### #   "])

(def beef-10
  ["#####  ###### ###### ######"
   "#    # #      #      #     "
   "#    # #      #      #     "
   "#    # #      #      #     "
   "#####  #####  #####  ##### "
   "#    # #      #      #     "
   "#    # #      #      #     "
   "#    # #      #      #     "
   "#    # #      #      #     "
   "#####  ###### ###### #     "])

(deftest ocr-test
  (testing "basic OCR"
    (is (= "A" (ascii-art/ocr letter-a-6)))
    (is (= "B" (ascii-art/ocr letter-b-6)))
    (is (= "AB" (ascii-art/ocr (map str letter-a-6 letter-b-6))))
    (is (= "BEEF" (ascii-art/ocr beef-6)))
    (is (= "BEEF" (ascii-art/ocr beef-10))))

  (testing "with custom on/off characters"
    (let [a-custom (map #(-> %
                             (str/replace \# \X)
                             (str/replace \space \_))
                        letter-a-6)]
      (is (= "A" (ascii-art/ocr a-custom :on \X)))))

  (testing "with vertical padding"
    (let [padded (concat ["     "] letter-a-6 ["     "])]
      (is (= "A" (ascii-art/ocr padded)))))

  (testing "unknown character"
    (let [unknown [" #   "
                   "#    "
                   "#    "
                   "#### "
                   "#  # "
                   "#  # "]]
      (is (= "?" (ascii-art/ocr unknown))))))
