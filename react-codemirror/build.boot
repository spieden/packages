(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[cljsjs/boot-cljsjs "0.5.2" :scope "test"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +lib-version+ "0.2.6")
(def codemirror-checksum "b96dceddf536a91a3c6b39fdd7f5d957")

(def +version+ (str +lib-version+ "-2"))

(task-options!
  pom  {:project     'cljsjs/react-codemirror
        :version     +version+
        :scm         {:url "https://github.com/cljsjs/packages"}
        :description "CodeMirror is a versatile text editor implemented in JavaScript for the browser (React edition)"
        :url         "https://github.com/JedWatson/react-codemirror/"
        :license     {"MIT" "https://github.com/JedWatson/react-codemirror/blob/master/LICENSE"}})

(require '[boot.core :as c]
         '[boot.tmpdir :as tmpd]
         '[clojure.java.io :as io]
         '[clojure.string :as string]
         '[boot.util :refer [sh]]
         '[boot.tmpdir :as tmpd])

(deftask package []
  (comp
    (download :url (format "https://github.com/JedWatson/react-codemirror/archive/v%s.zip" +lib-version+)
              :unzip true
              :checksum codemirror-checksum)
    (sift :move {
                 #"^react-codemirror-([\d\.]*)/dist/react-codemirror\.js"      "cljsjs/react-codemirror/development/react-codemirror.inc.js"
                 #"^react-codemirror-([\d\.]*)/dist/react-codemirror\.min\.js" "cljsjs/react-codemirror/production/react-codemirror.min.inc.js"
})
    (sift :include #{#"^cljsjs"})
    (deps-cljs :name "cljsjs.react-codemirror")
    (pom)
    (jar)))

