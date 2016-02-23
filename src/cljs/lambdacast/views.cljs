(ns lambdacast.views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :as reagent]
            [material-ui.core :as ui]
            [domina.css :as dom]
            [domina.events :refer [listen!]]
            [goog.dom :as gdom]
            [cljsjs.moment]))

(defn fmt-seconds [str]
  (-> (js/moment.utc (* str 1000)) (.format "HH:mm:ss")))

(defn goog-hash-map [object]
  (zipmap (goog.object/getKeys object) (goog.object/getValues object)))

(defn player-slider [p]
  [ui/slider {:style {:position "relative" :top "-6px"}
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
     (if (= s :playing) [:i.material-icons "pause"] [:i.material-icons "play_arrow"])]))

(defn player []
  (reagent/create-class
   {:component-did-mount
    (fn []
      (listen!
       (dom/sel "#player")
       :timeupdate
       (fn [_]
         (let [el (gdom/getElement "player")
               time (.-currentTime el)
               total (.-duration el)]
           (dispatch [:set-player-time time total])))))
    :reagent-render
    (fn []
      (let [episode (subscribe [:episode])
            p (subscribe [:player])]
        [:div.player
         [ui/paper {:style {:height 70}}
          [player-slider p]
          [:div.player-content
           [:div (fmt-seconds (:time @p))]
           [player-actions p]
           [:div (fmt-seconds (:total @p))]]
          [player-audio episode]]]))}))

(defn cover [img]
  [:div.cover {:style {:background-image (str "url(" img ")")}}
   [ui/paper {:class "cover-image"}
    [:img {:src img}]]] )

(defn podcast-content [podcast children]
  [ui/paper (merge {:z-depth 2 :class "list" :style {:z-index 1 :position "relative"}})
   [ui/card
    [ui/card-title (merge {:title (:title podcast)
                           :subtitle (-> podcast :description :long)})]
    children]])

(defn episodes-list [eps]
  [ui/list
   (for [episode eps]
     [ui/list-item (merge {:key (:title episode)
                           :primary-text (:title episode)
                           :on-click #(dispatch [:set-episode episode])})])])

(defn home-page []
  (let [podcast (subscribe [:podcast])
        episode (subscribe [:episode])]
    (fn []
      [:div
       [cover (:image @podcast)]
       [podcast-content @podcast
        [episodes-list (:episodes @podcast)]]])))

(defmulti pages identity)
(defmethod pages :home [] [home-page])
(defmethod pages :default [] [:div])

(defn main-panel []
  (let [active-route (subscribe [:active-route])]
    (fn []
      [ui/mui-theme-wrap
       [:div
        [ui/app-bar (merge {:title "Lambdacast"
                            :style {:background-color "#607D8B"}})]
        (pages @active-route)
        [player]]])))

