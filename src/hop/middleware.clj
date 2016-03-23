(ns hop.middleware)

(defn- add-directories [build dirs]
  (update build :directories (fnil into []) dirs))

(defn add-classpath-dirs [build]
  (add-directories build (concat (:source-paths build)
                                 (:resource-paths build)
                                 (:test-paths build))))
