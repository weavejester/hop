(ns hop.explain
  (:gen-class)
  (:require [clojure.pprint :as pp]))

(defn -main [raw-build task-name & args]
  (let [build (read-string raw-build)]
    (if-let [task (get-in build [:tasks task-name])]
      (pp/pprint task)
      (println "No such task:" task-name))))
