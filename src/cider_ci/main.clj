(ns cider-ci.main
  (:refer-clojure :exclude [str keyword])
  (:require [cider-ci.utils.core :refer [keyword str]])
  (:gen-class)
  (:require
    [cider-ci.utils.app :as app]
    [cider-ci.repository.main]
    [cider-ci.server]
    [cider-ci.web :as web]
    [logbug.catcher :as catcher]

    ))


(defn -main [& args]
  (catcher/snatch
    {:level :fatal
     :throwable Throwable
     :return-fn (fn [e] (System/exit -1))}
    (app/init :repository web/build-main-handler)
    (cider-ci.server/initialize)
    ))
