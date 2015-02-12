(ns road.router
  (:require [clojure.tools.macro :as macro]
            [clout.core :as clout]))


(defn- prepare-route [route]
  (cond
    (string? route)
    `(clout/route-compile ~route)
    (vector? route)
    `(clout/route-compile
       ~(first route)
       ~(apply hash-map (rest route)))
       :else
       `(if (string? ~route)
          (clout/route-compile ~route)
          ~route)))


(defn- if-route [route handler]
  (fn [request]
    (if (clout/route-matches route request)
      (handler request))))


(defn- if-method [method handler]
  (fn [request]
    (if (= method (. request getMethod)) 
           (handler request))))

(defn make-route  [method path handler]
  (if-method method
             (if-route path
                       (fn [request]
                         (handler request)))))

(defn compile-route [method path handler]
  `(make-route
     ~method ~(prepare-route path) ~handler))

(defn routing [request & handlers] 
  (some #(% request) handlers))

(defn routes [& handlers] 
  #(apply routing % handlers))

(defmacro defroutes [name & routes]
    (let [[name routes] (macro/name-with-attributes name routes)]
      `(def ~name (routes ~@routes))))


(defmacro GET [path handler]
  (compile-route "GET" path handler))

(defn route-test [msg]
  (println msg))

