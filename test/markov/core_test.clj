(ns markov.core-test
  (:use clojure.test
        markov.core))

(deftest check-build-from-coll

  (is (= {"A" {"B" 1}
          "B" {"C" 1}
          "C" {"D" 1}}
         (build-from-coll ["A" "B" "C" "D"]))
      "Builds from collection")

  (is (= {"A" {"A" 1/6
               "B" 1/3
               "C" 1/6
               "D" 1/3}
          "B" {"A" 1}
          "C" {"A" 1/2
               "C" 1/2}
          "D" {"B" 1/2
               "C" 1/2}}
         (build-from-coll ["A" "B" "A" "C" "C" "A" "A"
                           "D" "B" "A" "B" "A" "D" "C"]))
      "Builds from more complex collection")

  (is (= nil
         (build-from-coll {:a "A" :b "B" :c "C"}))
      "Discards non-sequential collections"))

(deftest check-build-from-string

  (is (= {"A" {"B" 1}
          "B" {"C" 1}
          "C" {"D" 1}}
         (build-from-string "A B C D"))
      "Builds from string"))

(deftest check-build-from-file

  (is (= {"A" {"B" 1}
          "B" {"C" 1}
          "C" {"D" 1}}
         (build-from-file "test/markov/test_file.txt"))
      "Builds from a file"))
