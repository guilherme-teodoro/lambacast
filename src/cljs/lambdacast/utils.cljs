(ns lambdacast.utils
  (:require [cljsjs.moment]))

(defn fmt-seconds [str]
  (-> (js/moment.utc (* str 1000)) (.format "HH:mm:ss")))

(defn goog-hash-map [object]
  (zipmap (goog.object/getKeys object) (goog.object/getValues object)))

