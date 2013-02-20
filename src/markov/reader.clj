(ns markov.reader
  "Functions for conversion of input data to collections."
  (:use [clojure.string :only [split]]))

(defn prepare-string [string]
  "Convert string to collection; split by whitespace."
  (split string #"\s+"))

(defn prepare-file [filepath]
  "Read given file and convert it to collection."
  (prepare-string (slurp filepath)))
