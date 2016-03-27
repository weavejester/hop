{:name "hop"
 :group "hop"
 :version "0.1.0-SNAPSHOT"
 :dependencies [[org.clojure/clojure "1.8.0"]
                [com.cemerick/pomegranate "0.3.0"]
                [meta-merge "0.1.1"]
                [medley "0.7.3"]]
 :tasks {"refresh" {:main hop.task.core}
         "lein"    {:main hop.task.leiningen, :args [~task], :aot :all}}}
