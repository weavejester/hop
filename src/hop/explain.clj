(ns hop.explain
  (:gen-class)
  (:require [clojure.pprint :as pp]))

(defn -main [build & args]
  (pp/pprint (read-string build)))
