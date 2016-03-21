(ns hop.core
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn- absolute-path [path]
  (.getAbsolutePath (io/file path)))

(defn classpath [{:keys [directories]}]
  (str/join java.io.File/pathSeparator (map absolute-path directories)))
