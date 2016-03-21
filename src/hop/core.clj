(ns hop.core
  (:require [cemerick.pomegranate.aether :as aether]
            [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(defn- absolute-path [path]
  (.getAbsolutePath (io/file path)))

(defn- classpath-dirs [{:keys [directories]}]
  (map absolute-path directories))

(def default-repositories
  (merge aether/maven-central {"clojars" "http://clojars.org/repo"}))

(defn- classpath-deps [{:keys [dependencies]}]
  (when dependencies
    (aether/dependency-files
     (aether/resolve-dependencies
      :coordinates  dependencies
      :repositories default-repositories))))

(defn classpath [project]
  (->> (concat (classpath-dirs project) (classpath-deps project))
       (str/join java.io.File/pathSeparator)))

(defn script [{:keys [main] :as project}]
  (str "java -cp '" (classpath project) "' clojure.main -m '" main "'"))

(defn -main [& args]
  (println (script (edn/read-string (slurp "project.edn")))))
