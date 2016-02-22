(ns lambdacast.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [re-frame.core :as re-frame]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]
            [lambdacast.routes :as routes]
            [lambdacast.handlers]
            [lambdacast.subs]
            [lambdacast.views :as views]
            [accountant.core :as accountant]))

(accountant/configure-navigation!)

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn init! []
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))
