# rbre

JRuby's Joni wrapper for Clojure

This project is still __experimental__.

## Examples

```clojure
(re-split (make-re "( )") "a b c") 
;; output: ["a" " " "b" " " "c"]

(re-split (make-re "\\s+|(\\.)") "AB CD.EF") 
;; output: ["AB" "CD" "." "EF"]

```

[![Clojars Project](https://img.shields.io/clojars/v/rbre.svg)](https://clojars.org/rbre)