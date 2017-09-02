(defproject hangman-airconsole "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.854"]
                 [reagent "0.7.0"]
                 [re-frame "0.10.1"]]

  :plugins [[lein-cljsbuild "1.1.5"]]

  :min-lein-version "2.5.0"

  :clean-targets ^{:protect false} ["target" "resources/public/js/"]

  :source-paths ["src"]
  :resource-paths ["resources"]

  :cljsbuild {:builds [{:id "screen"
                        :source-paths ["src" "env/dev"]
                        :figwheel {:on-jsload "dev.screen/on-js-load"}
                        :compiler {:main dev.screen
                                   :asset-path "/js/compiled/screen-out"
                                   :output-to "resources/public/js/screen.js"
                                   :output-dir "resources/public/js/compiled/screen-out"
                                   :source-map true
                                   :optimizations :none
                                   :pretty-print  true
                                   :preloads [devtools.preload]
                                   :external-config {:devtools/config {:features-to-install :all}}
                                   }}
                       {:id "controller"
                        :source-paths ["src" "env/dev"]
                        :figwheel {:on-jsload "dev.controller/on-js-load"}
                        :compiler {:main dev.controller
                                   :asset-path "/js/compiled/controller-out"
                                   :output-to "resources/public/js/controller.js"
                                   :output-dir "resources/public/js/compiled/controller-out"
                                   :source-map true
                                   :optimizations :none
                                   :pretty-print  true
                                   :preloads [devtools.preload]
                                   :external-config {:devtools/config {:features-to-install :all}}}}]}


  :figwheel {:http-server-root "public"
             :server-port 3449
             :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"]
             :css-dirs ["resources/public/css"]}

  :profiles {:dev {:repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

                   :dependencies [[binaryage/devtools "0.9.4"]
                                  [figwheel-sidecar "0.5.11"]
                                  [org.clojure/tools.nrepl "0.2.13"]
                                  [com.cemerick/piggieback "0.2.2"]]

                   :source-paths ["env/dev"]
                   :plugins [[lein-figwheel "0.5.11"]]
                   :env {:dev true}}})
