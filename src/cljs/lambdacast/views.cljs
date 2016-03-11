(ns lambdacast.views
  (:require [re-frame.core :refer [subscribe]]
            [material-ui.core :as ui]
            [lambdacast.components.podcast-page :refer [podcast-page]]
            [lambdacast.components.login :refer [login]]
            [lambdacast.components.player :refer [player]]))

(defmulti pages identity)
(defmethod pages :home [] [podcast-page])
(defmethod pages :login [] [login])
(defmethod pages :default [] [:div])

(defn main-view [children]
  [ui/mui-theme-wrap
   [:div
    [ui/app-bar (merge {:title "Lambdacast"
                        :style {:background-color "#607D8B"}})]
   children]])

(defn main-panel []
  (ui/set-palette! {:primary3Color (ui/color "tealA400")})
  (let [active-route (subscribe [:active-route])]
    (fn []
      [main-view
       [:div
        (pages @active-route)
        [player]]])))

