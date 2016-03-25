(ns hop.leiningen
  (:gen-class)
  (:require [clojure.java.io :as io]
            [leiningen.core.main :as main]
            [leiningen.core.project :as project]))

(def root-path
  (.getAbsolutePath (io/file ".")))

(defn- lein-project [build]
  (-> build
      (assoc :root root-path, :plugins [], :eval-in-leiningen true)
      (dissoc :args :directories :main :repositories)))

(defn -main [build & args]
  (let [project (project/init-project (lein-project (read-string build)))]
    (main/resolve-and-apply project args)
    (main/exit 0)))
