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

(defn make-re [raw-pat]
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

(defn match?
  ([re txt] (match? re txt 0))
  ([re txt pos] (let  [b-txt (.getBytes txt)
                       matcher (.matcher re b-txt)
                       len (count b-txt)
                       result (.match matcher pos len  Option/DEFAULT)]
                  (>= result 0))))

(defn split [re txt]
  (let [b-txt (.getBytes txt)
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


