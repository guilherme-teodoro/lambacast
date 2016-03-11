(ns lambdacast.db
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.conversion :refer [from-db-object]]))

(let [conn (mg/connect)
      db   (mg/get-db conn "lambdacast")
      coll "podcasts"]

  (defn get-podcasts []
    (from-db-object (mc/find-maps db coll) true))

  (defn get-podcast [t]
    (from-db-object (mc/find-one db coll {:title t}) true))

  (defn add-podcast [{:keys [title] :as data}]
    (if (empty? (get-podcast title))
      (mc/insert db coll data))))
