(ns markov.core
  "Markov chains.
   Given a sequential collection functions here compute the transition matrix.
   The result type is {previous_state {next_state probability}}."
  (:use [markov.reader]))

(defn- create-counts [coll]
  "Computes how many times did each 'next state' come from a 'previous state'.
   The result type is {previous_state {next_state count}}."
  (let [past    coll
        present (rest coll)
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

(defn build-from-coll [coll]
  "Computes a transition matrix of a collection."
  (if (sequential? coll)
    (create-probs (create-counts coll))))

(defn build-from-string [string]
  "Converts string to collection and computes its transition matrix."
  (build-from-coll (prepare-string string)))

(defn build-from-file [filepath]
  "Reads a file into a collection and computes its transition matrix."
  (build-from-coll (prepare-file filepath)))
