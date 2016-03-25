(ns hop.core
  (:gen-class)
  (:require [cemerick.pomegranate :as pomegranate]
            [cemerick.pomegranate.aether :as aether]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [meta-merge.core :refer [meta-merge]]))

(def buildfile "build.clj")

(def default-repositories
  (merge aether/maven-central {"clojars" "http://clojars.org/repo"}))

(def default-middleware
  '[hop.middleware/source-paths
    hop.middleware/resource-paths
    hop.middleware/test-paths
    hop.middleware/global-build-options
    hop.middleware/build-arguments])

(def default-jvm-opts
  ["-XX:+TieredCompilation"
   "-XX:TieredStopAtLevel=1"
   "-XX:-OmitStackTraceInFastThrow"])

(def default-tasks
  '{"version" {:main hop.version}})

(def default-build
  {:repositories default-repositories
   :middleware   default-middleware
   :jvm-opts     default-jvm-opts
   :tasks        default-tasks})

(defn- absolute-path [path]
  (.getAbsolutePath (io/file path)))

(defn- classpath-dirs [{:keys [directories]}]
  (map absolute-path directories))

(def ^:private resolve-deps
  (memoize
   (fn [dependencies repositories]
     (when dependencies
       (aether/dependency-files
        (aether/resolve-dependencies
         :coordinates  dependencies
         :repositories repositories))))))

(defn- classpath-deps [{:keys [dependencies repositories]}]
  (resolve-deps dependencies repositories))

(defn classpath [build]
  (->> (concat (classpath-dirs build) (classpath-deps build))
       (str/join java.io.File/pathSeparator)))

(defn- arglist [args]
  (if (seq args)
    (str " " (str/join " " (map pr-str args)))
    ""))

(defn- java-command [task]
  (str "$JAVA_CMD " (str/join " " (:jvm-opts task))
       " -cp " (pr-str (classpath task))
       " clojure.main -m " (pr-str (:main task))
       (arglist (:args task))))

(defn print-script [build]
  (println "case $1 in")
  (doseq [[name task] (:tasks build)]
    (println (str name ")"))
    (println "  shift")
    (println "  " (java-command task) "$@")
    (println "  ;;"))
  (println "esac"))

(defn load-plugins [{:keys [plugins repositories]}]
  (pomegranate/add-dependencies
   :coordinates  plugins
   :repositories repositories))

(defn- require-and-resolve [sym]
  (require (symbol (namespace sym)))
  (var-get (resolve sym)))

(defn apply-middleware [{:keys [middleware] :as build}]
  (load-plugins build)
  (->> middleware
       (map require-and-resolve)
       (reduce (fn [m f] (f m)) build)))

(defn read-buildfile []
  (-> buildfile slurp read-string))

(defn -main [& args]
  (->> (read-buildfile)
       (meta-merge default-build)
       (apply-middleware)
       (print-script)))
