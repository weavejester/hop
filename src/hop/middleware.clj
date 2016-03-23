(ns hop.middleware)

(defn- add-directories [build dirs]
  (update build :directories (fnil into []) dirs))

(defn source-paths [build]
  (add-directories build (:source-paths build)))

(defn resource-paths [build]
  (add-directories build (:resource-paths build)))

(defn test-paths [build]
  (add-directories build (:test-paths build)))
