(ns road.router
  (:require [clojure.tools.macro :as macro]
            [clout.core :as clout]))

(defn- convert [val type]
    (cond (= "Integer" type) (Integer/parseInt val)
                  (= "Float" type) (Float/parseFloat val)
                  (= "Double" type) (Double/parseDouble val)
                  :else val))

(defn- get-tag [arg]
    (-> arg meta :tag str))

(defn- convert-type-to-string [args]
    (-> (map str args) vec))

(defn- get-request-para [name req]
    (.getParameter req name))

(defn- get-para [arg req]
    (-> arg str (get-request-para req) (convert (get-tag arg))))


(defn- parse-arguments [f req]
    (println  (meta (var f)))
    (-> (meta (var f)) :arglists first (#(map get-para req)) vec))

(defn- prepare-route [route]
  #(re-matches (re-pattern route) %))


(defn- if-route [route handler]
  (fn [request]
    (if (route (.getContextPath request))
      (handler request))))


(defn- if-method [method handler]
  (fn [request]
    (if (= method (. request getMethod)) 
           (handler request))))

(defn- process-request [handler request]
  (apply handler (parse-arguments handler request)))

(defn make-route  [method path handler]
  (if-method method
             (if-route path
                       (fn [request]
                         (process-request handler request)))))

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


