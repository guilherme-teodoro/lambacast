(ns lambdacast.db)

(def default-db
  {:active-route ""
   :podcast {}
   :episode {}
   :player {:status :paused
            :time 0
            :total 0
            :percent 0}})
