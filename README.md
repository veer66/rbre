# rbre

JRuby's Joni wrapper for Clojure

This project is still __experimental__.

## Examples

```clojure
(require '[rbre.core :as rbre])

(rbre/split (rbre/make-re "( )") "a b c") 
;; output: ["a" " " "b" " " "c"]

(rbre/split (rbre/make-re "\\s+|(\\.)") "AB CD.EF") 
;; output: ["AB" "CD" "." "EF"]

```

[![Clojars Project](https://img.shields.io/clojars/v/rbre.svg)](https://clojars.org/rbre)
