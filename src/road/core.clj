(ns road.core 
  (:require [clojure.java.io :as io]
            [ring.util.response :as resp]
            [ring.middleware.params :as params]
            [road.render :as render]))

(defn route-handler [] (-> "web.clj" io/resource slurp load-string))

(defn get-template [ret] 
  (if-let [fname (:hiccup ret)] (-> (str "views/" fname) io/resource slurp)))

(defn handler [request] 
  (if-let [ret ((route-handler) (params/params-request request))] 
    (render/dispatch ret (get-template ret)) (resp/not-found "not found page")))

