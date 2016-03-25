(ns hop.middleware
  (:require [meta-merge.core :refer [meta-merge]]
            [medley.core :refer [map-vals]]))

(defn- add-directories [build dirs]
  (update build :directories (fnil into []) dirs))

(defn source-paths [build]
  (add-directories build (:source-paths build)))

(defn resource-paths [build]
  (add-directories build (:resource-paths build)))

(defn test-paths [build]
  (add-directories build (:test-paths build)))

(defn- update-task-options [tasks options]
  (map-vals (partial meta-merge options) tasks))

(defn global-build-options [build]
  (update build :tasks update-task-options (dissoc build :tasks)))
