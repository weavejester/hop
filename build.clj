{:source-paths ["src"]
 :dependencies [[org.clojure/clojure "1.8.0"]
                [com.cemerick/pomegranate "0.3.0"]
                [meta-merge "0.1.1"]
                [leiningen "2.6.1"]]
 :tasks {"refresh" {:main hop.core}
         "lein"    {:main hop.leiningen}}}
