(ns lambdacast.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [hiccup.page :refer [include-js include-css html5]]
            [lambdacast.middleware :refer [wrap-middleware]]
            [environ.core :refer [env]]
            [clojure.xml :as xml]
            [liberator.core :refer [defresource]]
            [lambdacast.db :as db])
  (:use [feedparser-clj.core]))

(def mount-target
  [:div#app
      [:h3 "ClojureScript has not been compiled!"]
      [:p "please run "
       [:b "lein figwheel"]
       " in order to start the compiler"]])

(def loading-page
  (html5
   [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1"}]
    (include-css (if (env :dev) "css/profile.css" "css/profile.min.css"))
    (include-css "https://fonts.googleapis.com/icon?family=Material+Icons")
    (include-js "https://cdn.rawgit.com/madvas/fractalify/master/resources/public/vendor/vendor.min.js")]
    [:body
     mount-target
     (include-js "js/app.js")]))

(defn response []
  (-> "http://feeds.feedburner.com/rapaduracast"
      parse-feed
      db/add-podcast))

(defresource get-podcasts
  :available-media-types ["text/plain"]
  :handle-ok (db/get-podcasts))

(defroutes routes
  (GET "/" [] loading-page)
  (GET "/about" [] loading-page)
  (GET "/login" [] loading-page)
  (GET "/api/podcasts" [] (get-podcasts))

  (resources "/")
  (not-found "Not Found"))

(def app (wrap-middleware #'routes))
