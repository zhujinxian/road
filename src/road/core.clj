(ns road.core 
  (:use [road.router]) 
  (:require [clojure.java.io :as io]
            [clojure.tools.macro :as macro]
            [clojure.tools.logging :as log]
            [ring.util.response :as resp]
            [ring.middleware.params :as params]
            [road.render :as render]))

(defn get-template [ret] 
  (if-let [fname (:hiccup ret)] (-> (str "views/" fname) io/resource slurp)))

(defmacro make-defroutes [name routes] 
  `(defroutes ~name ~@routes))

(defn cons-handler-render [route]
   #(if-let [ret ((params/wrap-params route) %1)]
     (render/dispatch ret (get-template ret)) (resp/not-found "not found page")))

(defmacro defroad [name & routes]
  `(def ~name (cons-handler-render (make-defroutes road-router# ~routes)))) 

(defmacro GET [path handler]
  (compile-route :get path handler))

(defmacro POST [path handler]
  (compile-route :post path handler))


