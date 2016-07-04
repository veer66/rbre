(defproject rbre "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.jruby.joni/joni "2.1.10"]]
  :main ^:skip-aot rbre.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
