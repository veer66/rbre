(ns rbre.core-test
  (:require [clojure.test :refer :all]
            [rbre.core :as rbre]))

(deftest split-test
  (testing "split empty"
    (is (= (rbre/split (rbre/make-re " ") "")
           [])))
  (testing "split simple"
    (is (= (rbre/split (rbre/make-re " ") "a b")
           ["a" "b"])))
  (testing "split 3"
    (is (= (rbre/split (rbre/make-re " ") "a b c")
           ["a" "b" "c"])))
  (testing "split with delimiter"
    (is (= (rbre/split (rbre/make-re "( )") "a b c")
           ["a" " " "b" " " "c"])))
  (testing "thai"
    (is (= (rbre/split (rbre/make-re "( )") "ปลา กิน มด")
           ["ปลา" " " "กิน" " " "มด"])))
  (testing "thai without captures"
    (is (= (rbre/split (rbre/make-re "\\s+") "ปลา กิน มด")
           ["ปลา" "กิน" "มด"])))
  (testing "partial capture"
    (is (= (rbre/split (rbre/make-re "\\s+|(\\.)") "AB CD.EF")
           ["AB" "CD" "." "EF"]))))
