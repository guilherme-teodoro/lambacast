(ns lambdacast.components.login
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [reagent.core :as r]
            [material-ui.core :as ui]
            [cljs.core.async :refer [put! chan <! >! buffer]]))

(defn init-hello []
  (.init js/hello
         (clj->js {:google "789493891711-6t3r4c55i07o3rks8si9ldhfld5m8fp8"})
         {"redirect_uri" "/login" :scope "email"}))

(defn cb-profile [profile]
  (print (.getAuthResponse js/hello "google"))
  (print (js->clj profile :keywordize-keys true)))

(defn get-profile []
  (-> (js/hello "google")
      (.api "me")
      (.then cb-profile)))

(defn handle-click []
  (.login (js/hello "google")
          (clj->js {:scope "email, profile"})
          get-profile))

(defn login []
  (r/create-class
   {:component-did-mount init-hello
    :reagent-render
    (fn []
       [ui/mui-theme-wrap
        [:div {:style {:display "flex"
                       :position "absolute"
                       :width "100%"
                       :height "100%"
                       :align-items "center"
                       :justify-content "center"
                       :flex-direction "column"}}
         [:h1 {:style {:font "40px sans-serif"
                       :margin-bottom "10px"}} "Lambdacast"]
         [ui/raised-button {:on-click handle-click
                            :label "Google Sign Up"
                            :background-color "#4B9FEE"
                            :labelColor "white"}]]])}))
