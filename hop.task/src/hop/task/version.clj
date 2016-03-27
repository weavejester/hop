(ns hop.task.version
  (:gen-class))

(def version
  "0.1.0-SNAPSHOT")

(defn -main [& args]
  (println "Hop" version "on Java"
           (System/getProperty "java.version")
           (System/getProperty "java.vm.name")))
