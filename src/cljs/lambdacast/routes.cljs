(ns lambdacast.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.history.Html5History
           goog.Uri)
  (:require [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [re-frame.core :as re-frame]))

(defn hook-browser-navigation! []
  (let [history (doto (Html5History.)
                  (events/listen
                   EventType/NAVIGATE
                   (fn [event]
                     (secretary/dispatch! (.-token event))))
                  (.setUseFragment false)
                  (.setPathPrefix "")
                  (.setEnabled true))]))

(defn app-routes []
  (defroute "/" []
    (re-frame/dispatch [:set-active-route :home])
    (re-frame/dispatch [:get-podcast]))
  (defroute "/login" []
    (re-frame/dispatch [:set-active-route :login]))
  (hook-browser-navigation!))

