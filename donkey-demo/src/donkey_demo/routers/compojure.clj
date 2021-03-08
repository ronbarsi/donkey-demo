(ns donkey-demo.routers.compojure
  (:require
    [compojure.core :refer :all]
    [donkey-demo.middlewares :refer [specific-middleware]]))

(defroutes compojure-app
           (GET "/comp" [] "Compojure route!"))

(def routes* [{:handler compojure-app
              :handler-mode :blocking
              :middleware [specific-middleware]}])