(ns lambdacast.views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :as reagent]
            [material-ui.core :as ui]))

(defn player []
  (let [episode (subscribe [:episode])]
    [:audio {:src (-> @episode :enclosure :url)
             :controls true}
     [:source {:type "audio/mpeg"}]]))


;; (defn item [_ e]
;;   [ui/list-item (merge {:key (:title e)
;;                         :primary-text (:title e)})])


(defn home-page []
  (let [podcast (subscribe [:podcast])
        episode (subscribe [:episode])]
    (fn []
      [:div
       [ui/paper (merge {:z-depth 2})
        [:h1 (:title @podcast)]
        [player]
        [:img {:src (:image @podcast) :width 100 :height 100}]
        [ui/list
         (for [e (:episodes @podcast)]
           [ui/list-item (merge {:key (:title e) :primary-text (:title e)})])]]
      ])))

(defmulti pages identity)
(defmethod pages :home [] [ui/mui-theme-wrap [home-page]])
(defmethod pages :default [] [:div])

(defn main-panel []
  (let [active-route (subscribe [:active-route])]
    (fn []
      (pages @active-route))))

