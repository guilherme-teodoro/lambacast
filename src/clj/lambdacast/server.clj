(ns lambdacast.server
  (:require [lambdacast.handler :refer [app]]
            [environ.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]]
            [monger.core :as mg]
            [monger.collection :as mc])
  (:gen-class)
  (:import [com.mongodb MongoOptions ServerAddress]))

(defn init-db []
  (let [conn (mg/connect)
        db (mg/get-db conn "lambdacast")]
    (print "VAIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIi")
    (print (mc/find-maps db "podcasts"))))

 (defn -main [& args]
   (let [port (Integer/parseInt (or (env :port) "3000"))]
     (do (init-db)
         (run-jetty app {:port port :join? false}))))

