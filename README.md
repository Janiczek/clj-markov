# markov

markov is a simple Clojure library for work with Markov chains.

Limitations:

- currently only order-1 chains
- currently only outputs probabilities for the collection, no "random walks"

## Usage

For example of usage look at `test/markov/core_test.clj`.

The main three functions you'll probably choose from are:

```clojure
user=> ; (build-from-coll coll)
user=> ; (build-from-string string)
user=> ; (build-from-file file)

user=> (build-from-coll ["A" "B" "X" "C" "X" "A" "X" "C"])
{"A" {"B" 1/2, "X" 1/2}, "B" {"X" 1}, "C" {"X" 1}, "X" {"A" 1/3, "C" 2/3}}

user=> (build-from-string "A B X C X A X C")
{"A" {"B" 1/2, "X" 1/2}, "B" {"X" 1}, "C" {"X" 1}, "X" {"A" 1/3, "C" 2/3}}

user=> (build-from-file "path/to/file.txt")
{"A" {"B" 1/2, "X" 1/2}, "B" {"X" 1}, "C" {"X" 1}, "X" {"A" 1/3, "C" 2/3}}
```

If you use `build-from-coll`, you're not limited to collections of strings. You can use numbers, keywords, whatever.

## TODO

- higher-order chains
- random generator of data from the chain

## License

Copyright © 2013 Martin Janiczek ([@janiczek](http://twitter.com/janiczek))

Distributed under the Eclipse Public License, the same as Clojure.
