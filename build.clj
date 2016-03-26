{:name "hop"
 :group "hop"
 :version "0.1.0-SNAPSHOT"
 :dependencies [[org.clojure/clojure "1.8.0"]
                [com.cemerick/pomegranate "0.3.0"]
                [meta-merge "0.1.1"]
                [medley "0.7.3"]
                [leiningen "2.6.1"]]
 :tasks {"refresh" {:main hop.core}
         "lein"    {:main hop.leiningen, :args [~task], :aot :all}}}
