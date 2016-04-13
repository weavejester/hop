(ns hop.task.version
  (:gen-class))

(def version "0.0.2")

(defn -main [& args]
  (println "Hop" version "on Java"
           (System/getProperty "java.version")
           (System/getProperty "java.vm.name")))
