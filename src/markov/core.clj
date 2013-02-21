(ns markov.core
  "Markov chains.
   Compute the transitional matrix and generate random walks from it."
  (:use [markov.reader]))

(defn create-counts [order coll]
  "Computes how many times did each 'next state' come from a 'previous state'.
  Order must be < (count coll).
  The result type is {previous_state {next_state count}}."
  (let [past    (butlast (map vec (partition order 1 coll)))
        present (drop order coll)
        zipped  (map vector past present)
        sorted  (sort zipped)
        grouped (group-by first sorted)
        seconds (map (fn [[key pairs]] [key (map second pairs)]) (seq grouped))
        freqs   (map (fn [[key secs]]  [key (frequencies secs)]) seconds)]
    (into {} freqs)))

(defn- create-totals [count-map]
  "Computes the number of occurences of each state.
   The result type is {state count}."
  (let [totals (map (fn [[key counts]] [key (apply + (vals counts))])
                    (seq count-map))]
    (into {} totals)))

(defn- create-probs [count-map]
  "Computes the probabilities of each of the transitions.
   That's done by normalizing their counts into interval <0,1>.
   The result type is {previous_state {next_state probability}}."
  (let [totals (create-totals count-map)
        probs  (map (fn [[key counts]]
                      (let [the-total (get totals key)]
                        [key (into {} (map (fn [[k c]] [k (/ c the-total)])
                                           (seq counts)))]))
                    (seq count-map))]
    (into {} probs)))

(defn- take-from-probs [probs]
  ; probs = {:a 1/2 :b 1/3 :c 1/6}
  ; pseq  = [[:a 1/2] [:b 1/3] [:c 1/6]]
  ; added = [[:a 1/2] [:b 5/6] [:c 1]]
  "Given a map of probabilities that add up to 1, takes one randomly."
  (let [rval  (rand)
        pseq  (reverse (sort-by second probs)) ; sorts and makes a seq, yay!
        added (rest (reduce #(conj %1 [(first %2) (+ (second (last %1))
                                                     (second %2))])
                            [[:whatever 0]]
                            pseq))]
    (first (first (drop-while #(> rval (second %)) added)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Public functions ;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn build-from-coll
  "Computes probabilities (a transition matrix) of given order from collection."
  ([coll] (build-from-coll 1 coll))
  ([order coll]
   (if (and (sequential? coll)
            (< order (count coll)))
     (create-probs (create-counts order coll)))))

(defn build-from-string
  "Converts string to collection and computes its transition matrix."
  ([string] (build-from-string 1 string))
  ([order string] (build-from-coll order (prepare-string string))))

(defn build-from-file
  "Reads a file into a collection and computes its transition matrix."
  ([filepath] (build-from-file 1 filepath))
  ([order filepath] (build-from-coll order (prepare-file filepath))))

(defn generate-walk
  "Generates a random 'walk' given the probabilities and optional starting values.
  Needs the order of probability matrix and length of starting collection to match.
  Can stop when it gets to a state it wasn't trained on!
  For example, ABACAD -> if we ever get to D, we end."
  ([probs] (generate-walk (first (rand-nth (seq probs)))
                              probs))
  ([start probs] 
   (if (= (type start) clojure.lang.Keyword)
     (generate-walk [start] probs)
     (let [order (count start)]
       (letfn [(lazy-walk [last-state]
                 (let [next-state (take-from-probs (get probs last-state))
                       next-args  (conj (rest last-state) next-state)]
                   (if (nil? next-state)
                     nil ; cons _ nil = (_)
                     (cons next-state (lazy-seq (lazy-walk next-args))))))]
         (lazy-cat start (lazy-walk start)))))))
