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
           :src (-> @episode :enclosure :url)
           :controls false
           :autoplay true}
   [:source {:type "audio/mpeg"}]])

(defn player-actions [p]
  (let [s (:status @p)]
    [:div.player-actions
     {:on-click (fn []
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
       (dispatch [:set-player-time time total])))))

(defn player []
  (r/create-class
   {:component-did-mount
    handle-time-ev
    :reagent-render
    (fn []
      (let [episode (subscribe [:episode])
            p (subscribe [:player])]
        [:div.player
         [ui/paper {:style {:height 70}}
          [player-slider p]
          [:div.player-content
           [:div (utils/fmt-seconds (:time @p))]
           [player-actions p]
           [:div (utils/fmt-seconds (:total @p))]]
          [player-audio episode]]]))}))
