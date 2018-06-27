(ns rbre.core-test
  (:require [clojure.test :refer :all]
            [rbre.core :as rbre]))

(deftest match?-test
  (testing "basic"
    (is (rbre/match? (rbre/compile-pattern "A+") "AAA")))
  (testing "basic not match"
    (is (not (rbre/match? (rbre/compile-pattern "A+") "BBB"))))
  (testing "basic not match (partially)"
    (is (not (rbre/match? (rbre/compile-pattern "AC") "AB"))))
  (testing "partially match"
    (is (rbre/match? (rbre/compile-pattern "A+") "AAAB")))
  (testing "empty string matches"
    (is (rbre/match? (rbre/compile-pattern "A*") "")))
  (testing "empty string does not match"
    (is (not (rbre/match? (rbre/compile-pattern "A+") ""))))
  (testing "match with pos"
    (is (rbre/match? (rbre/compile-pattern "ABC") "XABCD" 1)))
  (testing "not match with pos"
    (is (not (rbre/match? (rbre/compile-pattern "ABC") "XABCD" 2)))))

(deftest split-test
  (testing "split empty"
    (is (= (rbre/split (rbre/compile-pattern " ") "")
           [])))
  (testing "split simple"
    (is (= (rbre/split (rbre/compile-pattern " ") "a b")
           ["a" "b"])))
  (testing "split 3"
    (is (= (rbre/split (rbre/compile-pattern " ") "a b c")
           ["a" "b" "c"])))
  (testing "split with delimiter"
    (is (= (rbre/split (rbre/compile-pattern "( )") "a b c")
           ["a" " " "b" " " "c"])))
  (testing "thai"
    (is (= (rbre/split (rbre/compile-pattern "( )") "ปลา กิน มด")
           ["ปลา" " " "กิน" " " "มด"])))
  (testing "thai without captures"
    (is (= (rbre/split (rbre/compile-pattern "\\s+") "ปลา กิน มด")
           ["ปลา" "กิน" "มด"])))
  (testing "partial capture"
    (is (= (rbre/split (rbre/compile-pattern "\\s+|(\\.)") "AB CD.EF")
           ["AB" "CD" "." "EF"]))))
