(defproject rbre "0.1.0"
  :description "JRuby's Joni wrapper for Clojure"
  :url "https://gitlab.com/veer66/rbre"
  :license {:name "The 2-Clause BSD License"
            :url "https://opensource.org/licenses/BSD-2-Clause"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.jruby.joni/joni "2.1.10"]]
  :main ^:skip-aot rbre.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
