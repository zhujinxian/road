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

(defn- get-request-para [name req paras]
  (or (.getParameter req name) ((keyword name) paras)))

(defn- get-para [arg req paras]
  (println (str "arg: " arg)) 
  (if (= "req" arg) req 
    (-> arg str (get-request-para req paras) (convert (get-tag arg)))))

(defn- get-all-paras [req paras args]
  (map #(get-para % req paras) args))

(defn parse-arguments [f req paras]
  (-> (meta f) :arglists first (#(get-all-paras req paras %))  vec))

(defn- prepare-route [route]
  ;#(re-matches (re-pattern route) %))
  (clout/route-compile route))

(defn- if-route [route handler]
  (fn [request]
    (let [ret  (clout/route-matches route (.getServletPath request))]
      (println ret)
      (if-not (nil? ret) (handler request ret) (println "not found url"))))) 

(defn- if-method [method handler]
  (fn [request]
    (if (= method (. request getMethod)) 
           (handler request))))

(defn- process-request [handler request paras]
  (apply handler (parse-arguments handler request paras)))

(defn make-route  [method path handler]
  (if-method method
             (if-route path
                       (fn [request paras]
                         (process-request handler request paras)))))

(defn compile-route [method path handler]
  `(make-route
     ~method ~(prepare-route path) (var ~handler)))

(defn routing [request & handlers] 
  (some #(% request) handlers))

(defn routes [& handlers] 
  #(apply routing % handlers))

(defmacro defroutes [name & routes]
    (let [[name routes] (macro/name-with-attributes name routes)]
      `(def ~name (routes ~@routes))))

(defmacro GET [path handler]
  (compile-route "GET" path handler))

(defmacro POST [path handler]
  (compile-route "POST" path handler))

(defn route-test [msg]
  (println msg))


