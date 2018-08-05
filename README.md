# rbre

JRuby's Joni wrapper for Clojure

This project is still __experimental__. 
This project is supposed to provide A regular expression processor in Clojure that behaves similar to one in Ruby.

## Examples

```clojure
(require '[rbre.core :as rbre])

(rbre/split (rbre/comp-pat "( )") "a b c") 
;; output: ["a" " " "b" " " "c"]

(rbre/split (rbre/comp-pat "\\s+|(\\.)") "AB CD.EF") 
;; output: ["AB" "CD" "." "EF"]

(rbre/match? (rbre/comp-pat "A+") "AAA")
;; output: true

(rbre/match? (rbre/comp-pat "ABC") "XABCD" 2)
;; output: false

(rbre/match (rbre/comp-pat "⎈+☭") "卐⎈⎈⎈☭" -2)
;; output: ["⎈☭"]

```

[![Clojars Project](https://img.shields.io/clojars/v/rbre.svg)](https://clojars.org/rbre)
