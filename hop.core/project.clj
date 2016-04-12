(defproject hop/hop.core "0.0.1"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.cemerick/pomegranate "0.3.0"]
                 [meta-merge "0.1.1"]
                 [medley "0.7.3"]]
  :main hop.core
  :profiles {:uberjar {:aot :all}})
