(ns lambdacast.views
  (:require [re-frame.core :refer [subscribe]]
            [lambdacast.components.podcast-page :refer [podcast-page]]
            [lambdacast.components.login :refer [login]]
            [lambdacast.components.player :refer [player]]))

(defmulti pages identity)
(defmethod pages :home [] [podcast-page])
(defmethod pages :login [] [login])
(defmethod pages :default [] [:div])

(defn main-panel []
  (let [active-route (subscribe [:active-route])]
    (fn [] (pages @active-route))))

