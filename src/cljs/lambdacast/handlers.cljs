(ns lambdacast.handlers
  (:require [re-frame.core :as r :refer [register-handler dispatch]]
            [lambdacast.db :as db]
            [ajax.core :as ajax]
            [cognitect.transit :as t]))

(defn keywordize [handler]
  (fn imposter-handler [db [v data]]
    (handler db [v (clojure.walk/keywordize-keys data)])))

(register-handler
 :initialize-db
 (fn [_ _]
   db/default-db))

(register-handler
 :set-active-route
 (fn [db [_ active-route]]
   (assoc db :active-route active-route)))

(register-handler
 :get-podcast
 (fn [db _]
   (ajax/GET "http://localhost:3449/api"
       {:handler #(dispatch [:got-podcast %])
        :headers {:Accept ["application/transit+json"]}
        :format :transit
        :error-handler #(print "erro " %)})
   db))

(register-handler
 :got-podcast
 keywordize
 (fn [db [_ data]]
   (assoc db :podcast (t/read (t/reader :json) data))))

(register-handler
 :set-episode
 (fn [db [_ episode]]
   (assoc db :episode episode)))

(register-handler
 :set-player-time
 (fn [db [_ time total]]
   (let [percent (/ time total)]
     (assoc db :player {:time time
                        :total total
                        :percent percent
                        :status (-> db :player :status)}))))

(register-handler
 :set-player-status
 (fn [db [_ status]]
   (let [s (if (= status :playing) :paused :playing)]
     (assoc-in db [:player :status] s))))

