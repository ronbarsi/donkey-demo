(ns donkey-demo.middlewares)

(def global-middleware
  (fn [handler]
    (fn
      ([request]
         (println "GLOBAL MIDDLEWARE BLOCKING")
         (handler request))
      ([request respond raise]
       (try
         (println "GLOBAL MIDDLEWARE NON BLOCKING")
         (handler request respond raise)
         (catch Exception ex
           (raise ex)))))))

(def specific-middleware
  (fn [handler]
    (fn
      ([request]
         (println "PATH SPECIFIC MIDDLEWARE BLOCKING" (format "%s %s%s" (:request-method request) (:server-name request) (:uri request)))
         (handler request))
      ([request respond raise]
       (try
         (println "PATH SPECIFIC MIDDLEWARE NON BLOCKING" (format "%s %s%s" (:request-method request) (:server-name request) (:uri request)))
         (handler request respond raise)
         (catch Exception ex
           (raise ex)))))))

(def timing-middleware
  (fn [handler]
    (fn
      ([request]
       (let [start (System/currentTimeMillis)
             res   (handler request)
             end   (System/currentTimeMillis)]
         (println "TIME TOOK:" (- end start))
         res)
       (handler request))
      ([request respond raise]
       (try
         (let [start (System/currentTimeMillis)
               res   (handler request respond raise)
               end   (System/currentTimeMillis)]
           (println "TIME TOOK:" (- end start))
           res)
         (catch Exception ex
           (raise ex)))))))