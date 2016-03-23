(defproject hop "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.cemerick/pomegranate "0.3.0"]
                 [meta-merge "0.1.1"]]
  :main hop.core
  :uberjar-name "hop.jar"
  :profiles {:uberjar {:aot :all}})
