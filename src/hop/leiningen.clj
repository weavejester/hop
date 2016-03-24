(ns hop.leiningen
  (:gen-class)
  (:require [leiningen.version :as version]))

(defn -main [& args]
  (version/version {:eval-in-leiningen true}))
