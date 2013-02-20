(ns markov.reader-test
  (:use clojure.test
        markov.reader))

(deftest check-prepare-string

  (is (= ["A" "B" "C" "D"]
         (prepare-string "A B C D"))
      "Works at all?")

  (is (= ["A" "B" "C" "D"]
         (prepare-string "A\nB C\tD"))
      "Splits all common whitespace characters?")

  (is (= ["A" "B" "C" "D"]
         (prepare-string "A\nB  \tC\n\n\nD"))
      "Splits correctly even with consecutive whitespace characters?"))

(deftest check-prepare-file

  (is (= ["A" "B" "C" "D"]
         (prepare-file "test/markov/test_file.txt"))
      "Reads files correctly?"))
