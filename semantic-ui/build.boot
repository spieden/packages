(set-env! :resource-paths #{"resources"}
          :dependencies '[[cljsjs/boot-cljsjs "0.5.1" :scope "test"]
                          [cljsjs/jquery "2.1.4-0"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +lib-version+ "2.1.8")
(def +version+ (str +lib-version+ "-0"))

(task-options!
  pom {:project     'cljsjs/semantic-ui
       :version     +version+
       :description "User Interface is the language of the web"
       :url         "http://semantic-ui.com/"
       :scm         {:url "https://github.com/cljsjs/packages"}
       :license     {"MIT" "http://opensource.org/licenses/MIT"}})

(deftask package []
         (comp
           (download :url (str "https://github.com/Semantic-Org/Semantic-UI/archive/" +lib-version+ ".zip")
                     :checksum "866aa64df98e41459a9f88dadc6e8533"
                     :unzip true)
           (sift :move {#"^Semantic-UI-([\d\.]*)/dist/semantic\.js"                             "cljsjs/semantic-ui/development/semantic.inc.js"
                        #"^Semantic-UI-([\d\.]*)/dist/semantic\.min\.js"                        "cljsjs/semantic-ui/production/semantic.min.inc.js"
                        #"^Semantic-UI-([\d\.]*)/dist/semantic\.css"                            "cljsjs/semantic-ui/common/semantic.css"
                        #"^Semantic-UI-([\d\.]*)/dist/semantic\.min\.css"                       "cljsjs/semantic-ui/common/semantic.min.css"
                        #"^Semantic-UI-([\d\.]*)/dist/themes/default/assets/fonts/icons\.eot"   "cljsjs/semantic-ui/common/themes/default/assets/fonts/icons.eot"
                        #"^Semantic-UI-([\d\.]*)/dist/themes/default/assets/fonts/icons\.otf"   "cljsjs/semantic-ui/common/themes/default/assets/fonts/icons.otf"
                        #"^Semantic-UI-([\d\.]*)/dist/themes/default/assets/fonts/icons\.svg"   "cljsjs/semantic-ui/common/themes/default/assets/fonts/icons.svg"
                        #"^Semantic-UI-([\d\.]*)/dist/themes/default/assets/fonts/icons\.ttf"   "cljsjs/semantic-ui/common/themes/default/assets/fonts/icons.ttf"
                        #"^Semantic-UI-([\d\.]*)/dist/themes/default/assets/fonts/icons\.woff"  "cljsjs/semantic-ui/common/themes/default/assets/fonts/icons.woff"
                        #"^Semantic-UI-([\d\.]*)/dist/themes/default/assets/fonts/icons\.woff2" "cljsjs/semantic-ui/common/themes/default/assets/fonts/icons.woff2"
                        #"^Semantic-UI-([\d\.]*)/dist/themes/default/assets/images/flags.png"   "cljsjs/semantic-ui/common/themes/default/assets/images/flags.png"})
           (sift :include #{#"^cljsjs"})
           (deps-cljs :name "cljsjs.semantic-ui"
                      :requires ["cljsjs.jquery"])))

