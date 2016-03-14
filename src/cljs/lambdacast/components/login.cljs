(ns lambdacast.components.login
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [reagent.core :as r]
            [cljs.core.async :refer [put! chan <! >! buffer]]))

;; (def user (r/atom {}))

;; (defn load-gapi-auth2 []
;;   (let [c (chan)]
;;     (.load js/gapi "auth2" #(go (>! c true)))
;;     c))

;; (defn auth-instance []
;;   (.getAuthInstance js/gapi.auth2))

;; (defn get-google-token []
;;   (-> (auth-instance) .-currentUser .get .getAuthResponse .-id_token))

;; (defn handle-user-change
;;   [u]
;;   (let [profile (.getBasicProfile u)]
;;     (reset! user
;;             {:name       (if profile (.getName profile) nil)
;;              :image-url  (if profile (.getImageUrl profile) nil)
;;              :token      (get-google-token)
;;              :signed-in? (.isSignedIn u)})))

;; (defonce _ (go
;;              (<! (load-gapi-auth2))
;;              (.init js/gapi.auth2
;;                     (clj->js {"client_id" ".apps.googleusercontent.com"
;;                               "scope"     "profile email"}))
;;              (let [current-user (.-currentUser (auth-instance))]
;;                (.listen current-user handle-user-change))))


(defn teste []
  (.init js/hello
         (clj->js {:google "789493891711-6t3r4c55i07o3rks8si9ldhfld5m8fp8"})
         {"redirect_uri" "/login"
          "scope" "email"}))

(defn handl2 [p]
  (print (.getAuthResponse js/hello "google"))
  (print (js->clj p :keywordize-keys true)))

(defn handle []
  (-> (js/hello "google") (.api "me") (.then handl2)))

(defn logar []
  (.login (js/hello "google") (clj->js {:scope "email, profile"}) handle))

(defn login []
  (r/create-class
   {:component-did-mount teste
    :reagent-render
    (fn []
      [:div
         [:div#teste {:on-click logar} "olá"]])}))
