(ns lambdacast.handler
  (:require [compojure.core :refer [GET ANY defroutes]]
            [compojure.route :refer [not-found resources]]
            [hiccup.page :refer [include-js include-css html5]]
            [lambdacast.middleware :refer [wrap-middleware]]
            [environ.core :refer [env]]
            [lambdacast.resources :as rc]))

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
    [:meta {:name "google-signin-client_id" :content "468166379834-mcklql8qtnngno5dgfbo0q6g0p65n2jo.apps.googleusercontent.com"}]
    [:meta {:name "google-signin-scope" :content "profile"}]
    [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1"}]
    (include-css (if (env :dev) "css/profile.css" "css/profile.min.css"))
    (include-css "https://fonts.googleapis.com/icon?family=Material+Icons")
    (include-js "https://cdnjs.cloudflare.com/ajax/libs/hellojs/1.12.0/hello.all.min.js")
    (include-js "https://cdn.rawgit.com/madvas/fractalify/master/resources/public/vendor/vendor.min.js")]
    [:body
     mount-target
     (include-js "js/app.js")]))

(defroutes routes
  (GET "/" [] loading-page)
  (GET "/about" [] loading-page)
  (GET "/login" [] loading-page)
  (ANY "/api/podcasts" [] rc/list-podcasts)

  (resources "/")
  (not-found "Not Found"))

(def app (wrap-middleware #'routes))
