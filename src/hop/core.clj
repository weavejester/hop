(ns hop.core
  (:gen-class)
  (:require [cemerick.pomegranate.aether :as aether]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def build-filename "build.clj")

(def default-repositories
  (merge aether/maven-central {"clojars" "http://clojars.org/repo"}))

(defn- absolute-path [path]
  (.getAbsolutePath (io/file path)))

(defn- classpath-dirs [{:keys [directories]}]
  (map absolute-path directories))

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
  (-> build-filename slurp read-string script println))
