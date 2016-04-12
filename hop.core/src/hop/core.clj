(ns hop.core
  (:gen-class)
  (:require [cemerick.pomegranate :as pomegranate]
            [cemerick.pomegranate.aether :as aether]
            [clojure.string :as str]
            [meta-merge.core :refer [meta-merge]]))

(def buildfile "build.clj")

(def default-repositories
  (merge aether/maven-central {"clojars" "http://clojars.org/repo"}))

(def default-middleware
  '[hop.core.middleware/global-build-options
    hop.core.middleware/source-paths
    hop.core.middleware/resource-paths
    hop.core.middleware/test-paths
    hop.core.middleware/compile-path
    hop.core.middleware/clojure-tasks
    hop.core.middleware/build-arguments])

(def default-jvm-opts
  ["-XX:+TieredCompilation"
   "-XX:TieredStopAtLevel=1"
   "-XX:-OmitStackTraceInFastThrow"])

(defn- hop-task [m]
  (meta-merge '{:dependencies [[hop/hop.task "0.0.1"]]} m))

(defn- lein-task [m]
  (meta-merge '{:dependencies [[hop/hop.lein "0.0.1"]]
                :main hop.task.leiningen
                :args [~task]}
              m))

(def default-tasks
  {"version" (hop-task '{:main hop.task.version})
   "explain" (hop-task '{:main hop.task.explain, :args [~build]})
   "uberjar" (lein-task '{:args ["uberjar"]})})

(def default-build
  {:repositories   default-repositories
   :dependencies   []
   :middleware     default-middleware
   :jvm-opts       default-jvm-opts
   :tasks          default-tasks
   :source-paths   ["src"]
   :resource-paths ["resources"]
   :test-paths     ["test"]
   :compile-path   "target/classes"})

(defn- exec-command [task]
  (str/join " " (map pr-str (:exec task))))

(defn print-script [build]
  (println "case $1 in")
  (doseq [[name task] (:tasks build)]
    (println (str name ")"))
    (println "  shift")
    (println "  exec" (exec-command task) "$@")
    (println "  ;;"))
  (println "*)")
  (println "  echo \"No such task: $1\"")
  (println "  ;;")
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
