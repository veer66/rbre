;;
;; JRuby's Joni wrapper for Clojure
;;
;; Copyright (c) 2016, Vee Satayamas
;; All rights reserved.
;;
;; Redistribution and use in source and binary forms, with or without modification,
;; are permitted provided that the following conditions are met:
;;
;; 1. Redistributions of source code must retain the above copyright notice,
;; this list of conditions and the following disclaimer.
;;
;; 2. Redistributions in binary form must reproduce the above copyright notice,
;; this list of conditions and the following disclaimer in the documentation
;; and/or other materials provided with the distribution.
;;
;; THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
;; ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
;; WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
;; DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
;; FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
;; DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
;; SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
;; CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
;; OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
;; USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.



(ns rbre.core
  (:gen-class))

(require '[clojure.java.io :as io])
(import org.joni.Regex)
(import org.joni.Option)
(import org.jcodings.specific.UTF8Encoding)
(import java.util.Arrays)

(comment (require '[clojure.pprint :refer [pprint]]))

(defn compile-pattern [raw-pat]
  (let [pat (.getBytes raw-pat)]
    (Regex. pat 0 (count pat) Option/NONE UTF8Encoding/INSTANCE)))

(defn re-lowlevel-match [re b-txt offset]
  (let  [matcher (.matcher re b-txt)
         result (.search matcher offset (count b-txt)  Option/DEFAULT)]
    (if (= result -1)
      nil
      (.getEagerRegion matcher))))

(defn slice-to-string [b-txt s e]
  (when (>= s 0)
    (String. (Arrays/copyOfRange b-txt s e) "UTF-8")))

(defn region-to-string [region b-txt i]
  (let [begin-reg (aget (.-beg region) i)
        end-reg (aget (.-end region) i)]
    (slice-to-string b-txt begin-reg end-reg)))

(defn region-regis-to-toks [region b-txt toks]
  (loop [toks toks i 1]
    (if (< i (.-numRegs region))
      (let [tok (region-to-string region b-txt i)]
        (recur (if tok
                 (conj toks tok)
                 toks)
               (inc i)))
      toks)))


(defn re-search [matcher pos len]
  (.search matcher pos (- len pos) 0))

(defn b-len [b]
  (cond
    (= (bit-and 2r11110000 b) 2r11110000) 4
    (= (bit-and 2r11100000 b) 2r11100000) 3
    (= (bit-and 2r11000000 b) 2r11000000) 2
    (= (bit-and 2r10000000 b) 2r00000000) 1))

(defn ch-len [b-txt u8-i]
  (let [b (aget b-txt u8-i)]
    (b-len b)))

(defn to-utf8-idx [b-txt i]
  (reduce (fn [u8-i j] (+ u8-i (ch-len b-txt u8-i)))
          (range (inc i))))

(defn to-utf8-cnt-indice [b-txt]
  (let [len (count b-txt)]
    (loop [indice [] i 0]
      (if (< i len)
        (let [cnt (ch-len b-txt i)]
          (recur (conj indice cnt)
                 (+ i cnt)))
        indice))))

(defn create-match-data [matcher b-txt pos len]
  (let [s (.getBegin matcher)
        e (.getEnd matcher)]
    [(String. b-txt s (- e s))]))

(defn to-utf8-pos [b-txt pos]
  (if (< pos 0)
    (->> (to-utf8-cnt-indice b-txt)
         (drop-last pos)
         (reduce +))
    (to-utf8-idx b-txt pos)))

(defn match
  ([re txt] (match re txt 0))
  ([re txt pos] (let  [b-txt (.getBytes txt "UTF-8")
                       len (count b-txt)
                       pos (to-utf8-pos b-txt pos)
                       matcher (.matcher re b-txt)
                       search-result (re-search matcher pos len)]
                  (cond
                    (>= search-result 0) (create-match-data matcher b-txt pos len)
                    :else nil))))

(defn match?
  ([re txt] (match? re txt 0))
  ([re txt pos] (let [match-data (match re txt pos)]
                  (not (nil? match-data)))))

(defn split [re txt]
  (let [b-txt (.getBytes txt "UTF-8")
        len (count b-txt)]
    (loop [toks [] offset 0]
      (if (>= offset len)
        toks
        (let [region (re-lowlevel-match re b-txt offset)]
          (if (nil? region)
            (conj toks
                  (slice-to-string b-txt offset len))
            (let [begin-reg (aget (.-beg region) 0)
                  end-reg (aget (.-end region) 0)
                  tok (slice-to-string b-txt offset begin-reg)]
              (recur (region-regis-to-toks region
                                           b-txt
                                           (conj toks tok))
                     end-reg))))))))
