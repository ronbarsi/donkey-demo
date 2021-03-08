(ns donkey-demo.routers.donkey
  (:require [donkey-demo.middlewares :refer [specific-middleware timing-middleware]]))


(def example-route {:handler      (fn [request] {:body "Donkey route!"})
                    ;:handler      (fn [request respond raise] (respond {:body "Donkey route!"}))
                    ; A function that accepts 1 or 3 arguments (depending on :handler-mode). The function will be called if a request matches the route
                    ; The function should return a map with optional 3 fields: :status, :headers, :body (byte[], inputStream, or String)

                    :handler-mode :blocking
                    ; A contract where you declare the type of handling your route does - :blocking or :non-blocking (default)
                    ; Donkey server is built on top of vert.x which is based on Netty's architecture.
                    ; This architecture is based on the concept of a single threaded event loop that serves requests
                    ; :non-blocking means that the handler is performing very fast CPU-bound tasks, or non-blocking IO bound tasks.
                    ; In both cases the guarantee is that it will not block the event loop.
                    ; In :blocking handler mode the handler will be called on a separate worker thread pool without needing to worry about blocking the event loop
                    ; The worker thread pool size can be configured when creating a Donkey instance by setting the :worker-threads option

                    :path         "/donk"

                    :match-type   :simple
                    ; can be either :simple or :regex
                    ; :simple will take match in two ways: exact match OR with path parameters
                    ; (for example: the path "/user/:id" will match any combination, and :id will be available at the request under {:path-params {"id" ID}}
                    ;
                    ; :regex - For example, if wanted to only match the /user/:id path if :id is a number,
                    ; then we could use :match-type :regex and supply this path: /user/[0-9]+
                    ; you'll see the path param in the request {:path-params {"param0" VALUE}

                    :methods      [:get :post :delete]

                    :consumes     ["*/*"]
                    ; vector of media types that the handler can consume.
                    ; If a route matches but the Content-Type header of the request doesn't match one of the supported media types, then the request will be rejected with a 415 Unsupported Media Type code

                    :produces     ["application/json"]
                    ; vector of media types that the handler produces.
                    ; If a route matches but the Accept header of the request doesn't match one of the supported media types, then the request will be rejected with a 406 Not Acceptable code

                    :middleware   [specific-middleware]
                    ; vector of middleware functions that will be applied to the route.
                    ; It is also possible to supply a "global" :middleware vector when creating a server that will be applied to all the routes.
                    ; In that case the global middleware will be applied first, followed by the middleware specific to the route.
                    })

(def route-with-query-string {:path       "/greet"
                              :methods    [:get]
                              :handler-mode :blocking
                              :middleware   [specific-middleware]
                              :handler    (fn [req]
                                            {:body (str "Hello, " (get-in req [:query-params :fname]) " " (get-in req [:query-params :lname]))})})

(def non-blocking-route {:path         "/non-blocking-route"
                         :methods      [:get]
                         :handler-mode :non-blocking
                         :middleware   [timing-middleware]
                         :handler      (fn [req respond raise]
                                         (Thread/sleep 1000)
                                         (respond {:body "Non blocking route!"}))})

(def blocking-route {:path         "/blocking-route"
                     :methods      [:get]
                     :handler-mode :blocking
                     :middleware   [timing-middleware]
                     :handler      (fn [req]
                                     (Thread/sleep 1000)
                                     {:body "Blocking route!"})})

(def routes [example-route
             route-with-query-string
             non-blocking-route
             blocking-route])