(ns rbre.core-test
  (:require [clojure.test :refer :all]
            [rbre.core :refer :all]))

(deftest re-split-test
  (testing "re-split empty"
    (is (= (re-split (make-re " ") "")
           [])))
  (testing "re-split simple"
    (is (= (re-split (make-re " ") "a b")
           ["a" "b"])))
  (testing "re-split 3"
    (is (= (re-split (make-re " ") "a b c")
           ["a" "b" "c"])))
  (testing "re-split with delimiter"
    (is (= (re-split (make-re "( )") "a b c")
           ["a" " " "b" " " "c"])))
  (testing "thai"
    (is (= (re-split (make-re "( )") "ปลา กิน มด")
           ["ปลา" " " "กิน" " " "มด"]))))
