(ns lambacast.prod
  (:require [lambacast.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
