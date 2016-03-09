(ns lambdacast.components.player
  (:require [re-frame.core :refer [dispatch subscribe]]
            [domina.events :refer [listen!]]
            [material-ui.core :as ui]
            [reagent.core :as r]
            [goog.dom :as gdom]
            [domina.css :as dom]
            [lambdacast.utils :as utils]
            [lambdacast.components.icons :as i]))

(defn player-slider [p]
  [ui/slider
   {:style {:position "relative"
            :top "-6px"}
    :value (:percent @p)
    :on-change (fn [e v]
                 (let [r (* (:total @p) v)]
                   (set! (.-currentTime (gdom/getElement "player")) r)))}])

(defn player-audio [episode]
  [:audio {:id "player"
           :style {:position "relative" :top "-30px"}
           :src (-> @episode :link)
           :controls false
           :autoplay true}
   [:source {:type "audio/mpeg"}]])

(defn player-actions [p]
  (let [s (:status @p)]
    [:div.player-actions
     {:style {:cursor "pointer"}
      :on-click (fn []
                  (let [el (gdom/getElement "player")]
                    (dispatch [:set-player-status s])
                    (if (= s :playing) (.pause el) (.play el))))}
     (if (= s :playing) [i/pause] [i/play-arrow])]))

(defn- handle-time-ev []
  (listen!
   (dom/sel "#player")
   :timeupdate
   (fn [_]
     (let [el (gdom/getElement "player")
           time (.-currentTime el)
           total (.-duration el)]
       (print (.-duration el))
       (dispatch [:set-player-time time total])))))

(defn player []
  (r/create-class
   {:component-did-mount
    handle-time-ev
    :reagent-render
    (fn []
      (let [epi (subscribe [:episode])
            pl (subscribe [:player])]
        [:div.player
         [ui/paper {:style {:height 70}}
          [player-slider pl]
          [:div.player-content
           [:div {:style {:flex 1 :text-align "left"}} (:title @epi)]
           [player-actions pl]
           [:div {:style {:flex 1 :text-align "right"}} (str (utils/fmt-seconds (:time @pl))
                      "/"
                      (utils/fmt-seconds (:total @pl)))]]
          [player-audio epi]]]))}))
