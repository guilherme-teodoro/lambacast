(ns lambdacast.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

(register-sub
 :active-route
 (fn [db _]
   (reaction (:active-route @db))))

(register-sub
 :podcast
 (fn [db _]
   (reaction (:podcast @db))))

(register-sub
 :episode
 (fn [db _]
   (reaction (:episode @db))))

(register-sub
 :player
 (fn [db _]
   (reaction (:player @db))))

