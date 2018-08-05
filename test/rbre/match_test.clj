(ns rbre.match-test
  (:require [clojure.test :refer :all]
            [rbre.core :as rbre]))

(deftest match-test
  (testing "basic"
    (is (= (rbre/match (rbre/comp-pat "A+") "AAA")
           ["AAA"])))
  (testing "match partial"
    (is (= (rbre/match (rbre/comp-pat "A+B") "XXXAABC")
           ["AAB"])))
  (testing "match partial non-latin"
    (is (= (rbre/match (rbre/comp-pat "⎈+☭") "卐⎈⎈⎈☭")
           ["⎈⎈⎈☭"])))
  (testing "match pos"
    (is (= (rbre/match (rbre/comp-pat "⎈+☭") "卐⎈⎈⎈☭" 3)
           ["⎈☭"])))
  (testing "match pos reverse"
    (is (= (rbre/match (rbre/comp-pat "⎈+☭") "卐⎈⎈⎈☭" -2)
           ["⎈☭"]))))
