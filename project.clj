(defproject airconsole-game "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.854"]]

  :plugins [[lein-cljsbuild "1.1.5"]]

  :min-lein-version "2.5.0"

  :clean-targets ^{:protect false} ["resources/public/js/compiled/"]

  :source-paths ["src"]
  :resource-paths ["resources"]

  :cljsbuild {:builds {:app {:source-paths ["src" "env/dev"]
                             :figwheel {:on-jsload "user/on-js-load"}
                             :compiler {:main game.core
                                        :asset-path "/js/compiled/game-out"
                                        :output-to "resources/public/js/game.js"
                                        :output-dir "resources/public/js/compiled/game-out"
                                        :source-map true
                                        :optimizations :none
                                        :pretty-print  true}}}}


  :figwheel {:http-server-root "public"
             :server-port 3449
             :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"]
             :css-dirs ["resources/public/css"]}

  :profiles {:dev {:repl-options {:init-ns game.core
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

                   :dependencies [[binaryage/devtools "0.9.4"]
                                  [figwheel-sidecar "0.5.11"]
                                  [org.clojure/tools.nrepl "0.2.13"]
                                  [com.cemerick/piggieback "0.2.2"]]

                   :source-paths ["env/dev"]
                   :plugins [[lein-figwheel "0.5.11"]]
                   :env {:dev true}}})
