(ns hop.core
  (:gen-class)
  (:require [cemerick.pomegranate.aether :as aether]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [meta-merge.core :refer [meta-merge]]))

(def buildfile "build.clj")

(def default-repositories
  (merge aether/maven-central {"clojars" "http://clojars.org/repo"}))

(def default-build
  {:repositories default-repositories})

(defn- absolute-path [path]
  (.getAbsolutePath (io/file path)))

(defn- classpath-dirs [{:keys [directories]}]
  (map absolute-path directories))

(defn- classpath-deps [{:keys [dependencies repositories]}]
  (when dependencies
    (aether/dependency-files
     (aether/resolve-dependencies
      :coordinates  dependencies
      :repositories repositories))))

(defn classpath [project]
  (->> (concat (classpath-dirs project) (classpath-deps project))
       (str/join java.io.File/pathSeparator)))

(defn script [{:keys [main] :as project}]
  (str "java -cp '" (classpath project) "' clojure.main -m '" main "'"))

(defn read-buildfile []
  (->> buildfile slurp read-string (meta-merge default-build)))

(defn -main [& args]
  (println (script (read-buildfile))))
