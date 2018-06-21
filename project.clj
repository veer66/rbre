(defproject rbre "0.2.0"
  :description "JRuby's Joni wrapper for Clojure"
  :url "https://gitlab.com/veer66/rbre"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.jruby.joni/joni "2.1.10"]]
  :main ^:skip-aot rbre.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
