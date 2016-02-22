(ns lambacast.handlers
  (:require [re-frame.core :as r :refer [register-handler dispatch]]
            [lambacast.db :as db]
            [ajax.core :as ajax]))

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
   (ajax/GET "http://localhost:3000/api/"
       {:handler #(dispatch [:got-podcast %])
        :keywords? true})
   db))

(register-handler
 :got-podcast
 keywordize
 (fn [db [_ data]]
   (assoc db :podcast data)))

(register-handler
 :set-episode
 (fn [db [_ episode]]
   (assoc db :episode episode)))
