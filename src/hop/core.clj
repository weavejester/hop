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
  {:repositories default-repositories
   :jvm-opts     ["-XX:+TieredCompilation"
                  "-XX:TieredStopAtLevel=1"
                  "-XX:-OmitStackTraceInFastThrow"]})

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

(defn tasks [project]
  (for [[name task] (:tasks project)]
    (meta-merge (dissoc project :tasks) {:name name} task)))

(defn- java-command [task]
  (str "java " (str/join " " (:jvm-opts task))
       " -cp " (pr-str (classpath task))
       " clojure.main -m " (pr-str (:main task))))

(defn print-script [project]
  (println "case $1 in")
  (doseq [task (tasks project)]
    (println (str (:name task) ")"))
    (println "  " (java-command task))
    (println "  ;;"))
  (println "esac"))

(defn read-buildfile []
  (->> buildfile slurp read-string (meta-merge default-build)))

(defn -main [& args]
  (print-script (read-buildfile)))
