# markov

markov is a simple Clojure library for work with Markov chains.

Limitations:

- currently only outputs probabilities for the collection, no "random walks"

## Usage

For example of usage look at `test/markov/core_test.clj`.

The main three functions you'll probably choose from are:

```clojure
user=> ; (build-from-coll   order coll)
user=> ; (build-from-string order string)
user=> ; (build-from-file   order file)

user=> (build-from-coll 1 ["A" "B" "X" "B" "X" "A" "X" "C"])
{["A"] {"B" 1/2, "X" 1/2}, ["B"] {"X" 1}, ["X"] {"A" 1/3, "B" 1/3, "C" 1/3}}

user=> (build-from-string 1 "A B X B X A X C")
{["A"] {"B" 1/2, "X" 1/2}, ["B"] {"X" 1}, ["X"] {"A" 1/3, "B" 1/3, "C" 1/3}}

user=> (build-from-file 1 "path/to/file.txt")
{["A"] {"B" 1/2, "X" 1/2}, ["B"] {"X" 1}, ["X"] {"A" 1/3, "B" 1/3, "C" 1/3}}

user=> ; higher orders:

user=> (pprint (build-from-string 2 "A B X B X A X C"))
{["A" "B"] {"X" 1},
 ["A" "X"] {"C" 1},
 ["B" "X"] {"A" 1/2, "B" 1/2},
 ["X" "A"] {"X" 1},
 ["X" "B"] {"X" 1}}
nil

user=> ; other types than strings

user=> (build-from-coll 1 [1 3 2 2 1 2 3 1 2 1])
{[1] {2 2/3, 3 1/3}, [2] {1 1/2, 2 1/4, 3 1/4}, [3] {1 1/2, 2 1/2}}

user=> (pprint (build-from-coll 2 [1 3 2 2 1 2 3 1 2 1]))
{[1 2] {1 1/2, 3 1/2},
 [1 3] {2 1},
 [2 1] {2 1},
 [2 2] {1 1},
 [2 3] {1 1},
 [3 1] {2 1},
 [3 2] {2 1}}
nil
```

## TODO

- random generator of data from the chain

## License

Copyright © 2013 Martin Janiczek ([@janiczek](http://twitter.com/janiczek))

Distributed under the Eclipse Public License, the same as Clojure.
