(ns lambdacast.components.main
  (:require [material-ui.core :as ui]
            [lambdacast.components.player :as p]))

(defn main-view [children]
  (ui/set-palette! {:primary3Color (ui/color "tealA400")})
  (fn []
    [ui/mui-theme-wrap
     [:div
      [ui/app-bar (merge {:title "Lambdacast"
                          :style {:background-color "#607D8B"}})]
      children
      [p/player]]]))
