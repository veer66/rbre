(ns rbre.match-test
  (:require [clojure.test :refer :all]
            [rbre.core :as rbre]))

(deftest match-test
  (testing "basic"
    (is (= (rbre/match (rbre/compile "A+") "AAA")
           ["AAA"])))
  (testing "match partial"
    (is (= (rbre/match (rbre/compile "A+B") "XXXAABC")
           ["AAB"])))
  (testing "match partial non-latin"
    (is (= (rbre/match (rbre/compile "⎈+☭") "卐⎈⎈⎈☭")
           ["⎈⎈⎈☭"]))))
