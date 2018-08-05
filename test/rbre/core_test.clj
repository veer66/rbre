(ns rbre.core-test
  (:require [clojure.test :refer :all]
            [rbre.core :as rbre]))

(deftest match?-test
  (testing "basic"
    (is (rbre/match? (rbre/comp-pat "A+") "AAA")))
  (testing "basic not match"
    (is (not (rbre/match? (rbre/comp-pat "A+") "BBB"))))
  (testing "basic not match (partially)"
    (is (not (rbre/match? (rbre/comp-pat "AC") "AB"))))
  (testing "partially match"
    (is (rbre/match? (rbre/comp-pat "A+") "AAAB")))
  (testing "empty string matches"
    (is (rbre/match? (rbre/comp-pat "A*") "")))
  (testing "empty string does not match"
    (is (not (rbre/match? (rbre/comp-pat "A+") ""))))
  (testing "match with pos"
    (is (rbre/match? (rbre/comp-pat "ABC") "XABCD" 1)))
  (testing "not match with pos"
    (is (not (rbre/match? (rbre/comp-pat "ABC") "XABCD" 2)))))

(deftest split-test
  (testing "split empty"
    (is (= (rbre/split (rbre/comp-pat " ") "")
           [])))
  (testing "split simple"
    (is (= (rbre/split (rbre/comp-pat " ") "a b")
           ["a" "b"])))
  (testing "split 3"
    (is (= (rbre/split (rbre/comp-pat " ") "a b c")
           ["a" "b" "c"])))
  (testing "split with delimiter"
    (is (= (rbre/split (rbre/comp-pat "( )") "a b c")
           ["a" " " "b" " " "c"])))
  (testing "thai"
    (is (= (rbre/split (rbre/comp-pat "( )") "ปลา กิน มด")
           ["ปลา" " " "กิน" " " "มด"])))
  (testing "thai without captures"
    (is (= (rbre/split (rbre/comp-pat "\\s+") "ปลา กิน มด")
           ["ปลา" "กิน" "มด"])))
  (testing "partial capture"
    (is (= (rbre/split (rbre/comp-pat "\\s+|(\\.)") "AB CD.EF")
           ["AB" "CD" "." "EF"]))))
