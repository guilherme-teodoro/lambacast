(ns lambacast.views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :as reagent]))


(defn player []
  (let [episode (subscribe [:episode])]
    [:audio {:src (-> @episode :enclosure :url)
             :controls true}
     [:source {:type "audio/mpeg"}]]))

(defn home-page []
  (let [podcast (subscribe [:podcast])
        episode (subscribe [:episode])]
    [:div
     [:div (:title @episode)]
     [:div
      [player]]
     [:img {:src (:image @podcast) :width 100 :height 100}]
     (for [e (:episodes @podcast)]
       [:div {:key (:title e)
              :on-click #(dispatch [:set-episode e])}
        (:title e)])]))

(defmulti pages identity)
(defmethod pages :home [] [home-page])
(defmethod pages :default [] [:div])

(defn main-panel []
  (let [active-route (subscribe [:active-route])]
    (fn []
      (pages @active-route))))

