(defproject de.active-group/reacl-c-testing "0.1.0"
  :description "Library with utilities to write advanced tests of Reacl-c items and applications."
  :url "http://github.com/active-group/reacl-c-testing"
  
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.10.1" :scope "provided"]
                 [org.clojure/clojurescript "1.10.773" :scope "provided"]
                 [de.active-group/reacl-c "0.11.2"]
                 [de.active-group/active-clojure "0.36.0"]
                 [de.active-group/cljs-async "2.0.0"]]

  :plugins [[lein-codox "0.10.8"]
            [lein-auto "0.1.3"]]

  :profiles {:shadow {:dependencies [[thheller/shadow-cljs "2.11.7"]
                                     [binaryage/devtools "1.0.2"]]
                      :source-paths ["src" "test"]
                      :resource-paths ["target"]}
             
             :codox {:dependencies [[codox-theme-rdash "0.1.2"]]}}

  :clean-targets ^{:protect false} [:target-path]

  :aliases {"dev" ["with-profile" "shadow" "run" "-m" "shadow.cljs.devtools.cli" "watch" "test"]
            "build-test" ["with-profile" "shadow" "run" "-m" "shadow.cljs.devtools.cli" "compile" "ci"]
            ;; then run tests with: npx karma start --single-run
            }

  :codox {:language :clojurescript ;; :clojure
          :metadata {:doc/format :markdown}
          :themes [:rdash]
          :src-dir-uri "http://github.com/active-group/reacl-c-testing/blob/master/"
          :src-linenum-anchor-prefix "L"}

  :auto {:default {:paths ["src" "test"]}}
  )
