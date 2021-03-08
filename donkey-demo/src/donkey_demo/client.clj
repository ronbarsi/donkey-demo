(ns donkey-demo.client
  (:require [com.appsflyer.donkey.core :as donkey]
            [com.appsflyer.donkey.client :refer [request stop]]
            [com.appsflyer.donkey.result :refer [on-complete on-success on-fail]]
            [com.appsflyer.donkey.request :refer [submit submit-form submit-multipart-form]]))

; (require 'donkey-demo.client) (in-ns 'donkey-demo.client)
(defn donkey-client []
  (-> (donkey/create-donkey {:debug true})
      (donkey/create-client
        {:default-host               "localhost"
         :default-port               8080
         ;:ssl                        true
         :keep-alive                 true
         :keep-alive-timeout-seconds 30
         :connect-timeout-seconds    10
         :idle-timeout-seconds       20
         :enable-user-agent          true
         :user-agent                 "Donkey Server"
         :compression                true})))

(defn run []
  (let [client (donkey-client)]
    (-> client
        (request {:method :get
                  :uri    "/ping"})
        submit
        (on-fail (fn [ex] (println "Failed!" ex)))
        (on-success (fn [res] res))
        (on-complete
          (fn [res ex]
            (stop client)
            (if ex
              (println "Failed!" ex)
              res))))))

; Calling (def async-request (request donkey-client opts)) creates an AsyncRequest but does not submit the request yet.
; You can reuse an AsyncRequest instance to make the same request multiple times. There are several ways a request can be submitted:
;
;(submit async-request) GET request.
;(submit async-request body) raw body. body can be either a string, or a byte array. A typical use case would be POSTing serialized data such as JSON
;(submit-form async-request body) submits an urlencoded form. A Content-Type: application/x-www-form-urlencoded header will be added to the request, and the body will be urlencoded.
;(submit-multipart-form async-request body) submits a multipart form. A Content-Type: multipart/form-data header will be added to the request. Multipart forms can be used to send simple key-value attribute pairs, and uploading files.

