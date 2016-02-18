(set-env! :resource-paths #{"resources"}
          :dependencies '[[cljsjs/boot-cljsjs "0.5.1" :scope "test"]
                          [cljsjs/jquery "1.11.3-0"]
                          [cljsjs/jquery-ui "1.11.3-1"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +lib-version+ "0.9.3")
(def +version+ (str +lib-version+ "-0"))

(task-options!
  pom {:project     'cljsjs/igvjs
       :version     +version+
       :description "Lightweight HTML-5 versison of the Integrative Genomics Viewer"
       :url         "https://github.com/igvteam/igv.js"
       :scm         {:url "https://github.com/igvteam/igv.js"}
       :license     {"MIT" "http://opensource.org/licenses/MIT"}})

(deftask package []
         (comp
           (download :url (str "http://igv.org/web/release/" +lib-version+ "/igv-" +lib-version+ ".js"))
           (download :url (str "http://igv.org/web/release/" +lib-version+ "/igv-" +lib-version+ ".css"))
           (download :url (str "http://igv.org/web/release/" +lib-version+ "/img/igv_logo_letters_paths.svg"))
           (download :url "http://fonts.googleapis.com/css?family=PT+Sans:400,700"
                     :name "pt_sans.css")
           (download :url "http://fonts.googleapis.com/css?family=Open+Sans"
                     :name "open_sans.css")
           (download :url "http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css"
                     :name "font_awesome.css")
           (download :url "http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/fonts/fontawesome-webfont.eot?v=4.2.0"
                     :name "fontawesome-webfont.eot")
           (download :url "http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/fonts/fontawesome-webfont.woff?v=4.2.0"
                     :name "fontawesome-webfont.woff")
           (download :url "http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/fonts/fontawesome-webfont.ttf?v=4.2.0"
                     :name "fontawesome-webfont.ttf")
           (download :url "http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/fonts/fontawesome-webfont.svg?v=4.2.0"
                     :name "fontawesome-webfont.svg")
           (download :url "http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.3/themes/smoothness/jquery-ui.css"
                     :name "jquery_ui_theme.css")
           (sift :move {#"igv-[\d\.]+.js"              "cljsjs/igvjs/production/igv.inc.js"
                        #"igv-[\d\.]+.css"             "cljsjs/igvjs/common/igv.css"
                        ; FIXME also includes version specific file from the previous line
                        #"^([^/]+)\.css$"                  "cljsjs/igvjs/common/$1.css"
                        #"(fontawesome-webfont\..+)$"  "cljsjs/igvjs/common/fonts/$1"
                        #"igv_logo_letters_paths\.svg" "cljsjs/igvjs/common/igv_logo_letters_paths.svg"})
           (sift :include #{#"^cljsjs"})
           (deps-cljs :name "cljsjs.igvjs"
                      :requires ["cljsjs.jquery"
                                 "cljsjs.jquery-ui"])))

