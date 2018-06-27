(ns rbre.match-test
  (:require [clojure.test :refer :all]
            [rbre.core :as rbre]))

(deftest match-test
  (testing "basic"
    (is (= (rbre/match (rbre/compile-pattern "A+") "AAA")
           ["AAA"])))
  (testing "match partial"
    (is (= (rbre/match (rbre/compile-pattern "A+B") "XXXAABC")
           ["AAB"])))
  (testing "match partial non-latin"
    (is (= (rbre/match (rbre/compile-pattern "⎈+☭") "卐⎈⎈⎈☭")
           ["⎈⎈⎈☭"])))
  (testing "match pos"
    (is (= (rbre/match (rbre/compile-pattern "⎈+☭") "卐⎈⎈⎈☭" 3)
           ["⎈☭"])))
  (testing "match pos reverse"
    (is (= (rbre/match (rbre/compile-pattern "⎈+☭") "卐⎈⎈⎈☭" -2)
           ["⎈☭"]))))
