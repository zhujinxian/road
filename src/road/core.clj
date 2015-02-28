(ns road.core 
  (:use [road.router]) 
  (:require [clojure.java.io :as io]
            [clojure.test :as test]
            [ring.util.response :as resp]
            [ring.middleware.params :as params]
            [road.render :as render]))

(defn route-handler [] (-> "web.clj" io/resource slurp load-string))

(defn get-template [ret] 
  (if-let [fname (:hiccup ret)] (-> (str "views/" fname) io/resource slurp)))


(defn handler [request] 
  (if-let [ret ((route-handler) (params/params-request request))] 
    (render/dispatch ret (get-template ret)) (resp/not-found "not found page")))

(defmacro make-defroutes [name routes] 
  `(defroutes ~name ~@routes))

(defn handler-fun [opts route]
   #(if-let [ret (route (params/params-request %1))]
     (render/dispatch ret (get-template ret)) (resp/not-found "not found page")))

(defmacro make-road-handler [opts & routes]
  (let [mhandler (make-defroutes app routes)] (handle-fun opts mhandler))) 

