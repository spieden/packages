(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[cljsjs/boot-cljsjs "0.5.2"  :scope "test"]
                  [cljsjs/react "15.2.0-0"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all]
         '[boot.core :as boot]
         '[boot.tmpdir :as tmpd]
         '[clojure.java.io :as io]
         '[boot.util :refer [sh]])

(def +lib-version+ "7.11.8")
(def +version+ (str +lib-version+ "-0"))

(task-options!
 pom  {:project     'cljsjs/react-virtualized
       :version     +version+
       :description "React components for efficiently rendering large lists and tabular data."
       :url         "https://bvaughn.github.io/react-virtualized/"
       :scm         {:url "https://github.com/cljsjs/packages"}
       :license     {"MIT" "http://opensource.org/licenses/MIT"}})

(deftask build-react-virtualized []
  (let [tmp (boot/tmp-dir!)]
           (with-pre-wrap
             fileset
             ;; Copy all files in fileset to temp directory
             (doseq [f (->> fileset boot/input-files)
                     :let [target (io/file tmp (tmpd/path f))]]
               (io/make-parents target)
               (io/copy (tmpd/file f) target))
             (binding [boot.util/*sh-dir* (str (io/file tmp (format "react-virtualized-%s" +lib-version+)))]
               ((sh "npm" "install" "--ignore-scripts"))
               ((sh "npm" "run" "build:umd"))
               ((sh "npm" "run" "build:css")))
             (-> fileset (boot/add-resource tmp) boot/commit!))))

(deftask package []
  (comp
   (download :url (str "https://github.com/bvaughn/react-virtualized/archive/" +lib-version+ ".zip")
             :checksum "66fab54d15aaa7cad208046ee9cc2b95"
             :unzip true)
   (build-react-virtualized)
   (sift :move {#"^react-virtualized-(.*)/dist/umd/react-virtualized.js$" "cljsjs/react-virtualized/development/react-virtualized.inc.js"
                #"^react-virtualized-(.*)/styles.css$" "cljsjs/react-virtualized/common/react-virtualized.inc.css"})
   (minify :in "cljsjs/react-virtualized/development/react-virtualized.inc.js"
            :out "cljsjs/react-virtualized/production/react-virtualized.min.inc.js")
   (sift :include #{#"^cljsjs"})

   (deps-cljs :name "cljsjs.react-virtualized"
              :requires ["cljsjs.react"])
   (pom)

   (jar)))
