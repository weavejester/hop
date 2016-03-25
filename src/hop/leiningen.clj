(ns hop.leiningen
  (:gen-class)
  (:require [clojure.java.io :as io]
            [leiningen.core.main :as main]
            [leiningen.core.project :as project]))

(defn- lein-project [build]
  (-> build
      (assoc :root (io/file "."), :plugins [], :eval-in-leiningen true)
      (dissoc :directories, :main, :args :repositories)))

(defn -main [build & args]
  (let [project (project/init-project (lein-project (read-string build)))]
    (main/resolve-and-apply project args)
    (main/exit 0)))
