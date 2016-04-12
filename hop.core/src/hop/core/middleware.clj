(ns hop.core.middleware
  (:require [cemerick.pomegranate.aether :as aether]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [meta-merge.core :refer [meta-merge]]
            [medley.core :refer [map-vals]]))

(defn- update-tasks [build f & args]
  (update build :tasks (partial map-vals #(apply f % args))))

(defn- update-task-options [tasks options]
  (map-vals (partial meta-merge options) tasks))

(defn global-build-options [build]
  (update-tasks build (partial meta-merge (dissoc build :middleware :tasks))))

(defn- add-directories [task dirs]
  (update task :directories (fnil into []) dirs))

(defn source-paths [build]
  (update-tasks build #(add-directories % (:source-paths %))))

(defn resource-paths [build]
  (update-tasks build #(add-directories % (:resource-paths %))))

(defn test-paths [build]
  (update-tasks build #(add-directories % (:test-paths %))))

(defn compile-path [build]
  (update-tasks
   build
   (fn [{path :compile-path :as task}]
     (-> task
         (update :directories (fnil conj []) path)
         (update :jvm-opts (fnil conj []) (str "-Dclojure.compile.path=" path))))))

(defn- replace-build-arguments [build task]
  (let [mappings {'~build (pr-str build)
                  '~task  (pr-str task)}]
    (update task :exec (partial mapv #(mappings % %)))))

(defn build-arguments [build]
  (update build :tasks (partial map-vals (partial replace-build-arguments build))))

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

(defn- clojure-command [task]
  `["$JAVA_CMD" ~@(:jvm-opts task)
    "-cp" ~(classpath task)
    "clojure.main" "-m" ~(:main task) ~@(:args task)])

(defn clojure-tasks [build]
  (update-tasks
   build
   (fn [task]
     (if-not (:exec task)
       (assoc task :exec (clojure-command task))
       task))))
