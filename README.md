# markov

markov is a simple Clojure library for work with Markov chains.

## Usage

For more examples of usage look at `test/markov/core_test.clj`.

The main functions you'll use are:

```clojure
user=> ; Computing the probabilities ("training" the Markov chain)
user=> ; (build-from-coll   [order] coll)
user=> ; (build-from-string [order] string)
user=> ; (build-from-file   [order] file)

user=> ; Generating a random walk from them
user=> ; (generate-walk [start-value] probabilities)
```

Computing the probabilities:

```clojure
user=> (build-from-coll [:a :b :c]) ; default order 1
{[:a] {:b 1},
 [:b] {:c 1}}

user=> (build-from-string 2 "A B C A C A B") ; you can set the order
{["A" "B"] {"C" 1},                          ; also, we separate by whitespace
 ["A" "C"] {"A" 1},
 ["B" "C"] {"A" 1},
 ["C" "A"] {"B" 1/2,  ; there's both CAB
            "C" 1/2}} ;          and CAC

user=> (slurp "path/to/file.txt")
"A B\nC\n\nD\tC  D\n\tB"
user=> (build-from-file "path/to/file.txt")
{["A"] {"B" 1},
 ["B"] {"C" 1},
 ["C"] {"D" 1},
 ["D"] {"B" 1/2,
        "C" 1/2}}
```

Generating a random walk from them (it can actually be infinite, so we `take` from it):

```clojure
user=> (take 10 (generate-walk (build-from-string "A B A C A A")))
("A" "B" "A" "A" "B" "A" "B" "A" "C" "A")
```

As you can see, after every B or C there's an A, because when "training" the chain everytime it encountered B, there was A afterwards.

There can be halting rule: an element that is encountered first time on the end of the training collection:

```clojure
user=> (generate-walk (build-from-string "A B A C A D"))
("C" "A" "C" "A" "D") ; so, if we ever land on D, we end.
```

We can give it a starting value(s), if we wish:

```clojure
user=> (generate-walk "D" (build-from-string "A B A C A D"))
("D") ; now that was cruel.

user=> (generate-walk "C" (build-from-string "A B A C A D"))
("C" "A" "C" "A" "C" "A" "C" "A" "C" "A" "C" "A" "C" "A" "B" "A" "D")
; slightly better ;)

user=> (take 10 (generate-walk [1 2 3] (build-from-coll [4 3 2 1 2 3 2 1 2 1])))
(1 2 3 2 3 2 1 2 3 2) ; starting values match!
```

## TODO

Possible improvements:

- add more training data to a transition matrix

## License

Copyright © 2013 Martin Janiczek ([@janiczek](http://twitter.com/janiczek))

Distributed under the Eclipse Public License, the same as Clojure.
