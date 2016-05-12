(ns markov.core-test
  (:use clojure.test
        markov.core))

(deftest check-build-from-coll

  (is (= {["A"] {"B" 1}
          ["B"] {"C" 1}
          ["C"] {"D" 1}}
         (build-from-coll 1 ["A" "B" "C" "D"]))
      "Works with order 1")

  (is (= {["A"] {"B" 1}
          ["B"] {"C" 1}
          ["C"] {"D" 1}}
         (build-from-coll ["A" "B" "C" "D"]))
      "Defaultly has order 1")

  (is (= {["A" "B"] {"C" 1}
          ["B" "C"] {"D" 1}}
         (build-from-coll 2 ["A" "B" "C" "D"]))
      "Works with bigger orders")

  (is (= {["A"] {"A" 1/6
                 "B" 1/3
                 "C" 1/6
                 "D" 1/3}
          ["B"] {"A" 1}
          ["C"] {"A" 1/2
                 "C" 1/2}
          ["D"] {"B" 1/2
                 "C" 1/2}}
         (build-from-coll 1 ["A" "B" "A" "C" "C" "A" "A"
                             "D" "B" "A" "B" "A" "D" "C"]))
      "Builds from more complex collection")

  (is (= {["A" "A"] {"D" 1}
          ["A" "B"] {"A" 1}
          ["A" "C"] {"C" 1}
          ["A" "D"] {"B" 1/2
                     "C" 1/2}
          ["B" "A"] {"B" 1/3
                     "C" 1/3
                     "D" 1/3}
          ["C" "A"] {"A" 1}
          ["C" "C"] {"A" 1}
          ["D" "B"] {"A" 1}}
         (build-from-coll 2 ["A" "B" "A" "C" "C" "A" "A"
                             "D" "B" "A" "B" "A" "D" "C"]))
      "Higher orders and more complex collections")

  (is (= {[1] {2 2/3, 3 1/3}, [2] {1 1/2, 2 1/4, 3 1/4}, [3] {1 1/2, 2 1/2}}
         (build-from-coll 1 [1 3 2 2 1 2 3 1 2 1]))
      "Other types than strings")

  (is (= nil
         (build-from-coll 1 {:a "A" :b "B" :c "C"}))
      "Discards non-sequential collections")

  (is (= nil
         (build-from-coll 4 ["A" "B" "C" "D"]))
      "Discards meaningless orders"))

(deftest check-build-from-string

  (is (= {["A"] {"B" 1}
          ["B"] {"C" 1}
          ["C"] {"D" 1}}
         (build-from-string 1 "A B C D"))
      "Builds from string"))

(deftest check-build-from-file

  (is (= {["A"] {"B" 1}
          ["B"] {"C" 1}
          ["C"] {"D" 1}}
         (build-from-file 1 "test/markov/test_file.txt"))
      "Builds from a file"))

(deftest check-generate-walk

  (is (= :a
         (first (generate-walk :a (build-from-coll [:a :b :c :d :a :b :c :d]))))
      "If requested, starts with the first element")

  (is (= :a
         (first (generate-walk [:a] (build-from-coll [:a :b :c :d :a :b :c :d]))))
      "The starting value can be single element or collection")

  (is (= '(:a :b)
         (take 2 (generate-walk [:a :b] (build-from-coll [:a :b :c :d :a :b :c]))))
      "More starting values are possible")

  (is (= :d
         (last (generate-walk (build-from-coll [:a :b :a :c :a :d]))))
      "Halts if gets to state from which it didn't ever continue"))
