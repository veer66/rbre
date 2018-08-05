(defproject rbre "0.4.1"
  :description "rbre provides a Ruby-like regular expression processor in Clojure by wrapping JRuby's Joni"
  :url "https://gitlab.com/veer66/rbre"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.jruby.joni/joni "2.1.10"]]
  :main ^:skip-aot rbre.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
