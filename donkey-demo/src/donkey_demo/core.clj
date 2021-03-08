(ns donkey-demo.core
  (:require [com.appsflyer.donkey.core :refer [create-donkey create-server]]
            [com.appsflyer.donkey.server :refer [start start-sync]]
            [com.appsflyer.donkey.result :refer [on-success]]
            [donkey-demo.routers.compojure :as compojure]
            [donkey-demo.routers.donkey :as my-donkey]
            [donkey-demo.middlewares :refer [global-middleware]]
            [com.appsflyer.donkey.middleware.params :refer [parse-query-params]])
  (:gen-class))


(defn -main
  [& args]
  (->
    (create-donkey {:worker-threads 1})
    (create-server {:port       8080
                    :routes     (concat
                                  my-donkey/routes
                                  compojure/routes*)
                    :middleware [global-middleware
                                 (parse-query-params {:keywordize true})]})
    start  ;or `start-sync` - block the current thread execution until the server is running
    (on-success (fn [_] (println "Server started listening on port 8080")))))

