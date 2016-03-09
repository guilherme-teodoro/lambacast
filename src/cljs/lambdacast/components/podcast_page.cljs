(ns lambdacast.components.podcast-page
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :as reagent]
            [material-ui.core :as ui]
            [domina.css :as dom]
            [goog.dom :as gdom]
            [lambdacast.utils :as utils]))

(defn cover [img]
  [:div.cover {:style {:background-image (str "url(" img ")")}}
   [ui/paper {:class "cover-image"}
    [:img {:src img}]]])

(defn podcast-content [podcast children]
  [ui/paper {:z-depth 2
             :class "list"
             :style {:z-index 1 :position "relative"}}
   [ui/card
    [ui/card-title {:title (:title podcast)
                    :subtitle (-> podcast :description :long)}]
    children]])

(defn episodes-list [eps]
  [ui/list
   (for [episode eps]
     [ui/list-item {:key (:title episode)
                    :primary-text (:title episode)
                    :on-click #(dispatch [:set-episode episode])}])])

(defn podcast-page []
  (let [podcast (subscribe [:podcast])
        episode (subscribe [:episode])]
    (fn []
      (print @podcast)
      [:div
       [cover (-> @podcast :image :url)]
       [podcast-content @podcast
        [episodes-list (:entries @podcast)]]])))
