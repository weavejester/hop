(ns hop.middleware
  (:require [meta-merge.core :refer [meta-merge]]
            [medley.core :refer [map-vals]]))

(defn- update-tasks [build f & args]
  (update build :tasks (partial map-vals #(apply f % args))))

(defn- update-task-options [tasks options]
  (map-vals (partial meta-merge options) tasks))

(defn global-build-options [build]
  (update-tasks build (partial meta-merge (dissoc build :tasks))))

(defn- add-directories [build dirs]
  (update build :directories (fnil into []) dirs))

(defn source-paths [build]
  (update-tasks build add-directories (:source-paths build)))

(defn resource-paths [build]
  (update-tasks build add-directories (:resource-paths build)))

(defn test-paths [build]
  (update-tasks build add-directories (:test-paths build)))

(defn- replace-build-arguments [build task]
  (let [mappings {'~build (pr-str build)
                  '~task  (pr-str task)}]
    (update task :args (partial mapv #(mappings % %)))))

(defn build-arguments [build]
  (update build :tasks (partial map-vals (partial replace-build-arguments build))))
