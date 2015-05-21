(ns road.render
  (:require [clojure.string :as string]
            [hiccup.page :as page]
            [ring.util.response :as resp]))

(defn text [text]
  (-> (resp/response text) 
    (resp/content-type "text/plain") (resp/charset "utf-8")))

(defn json [text]
  (-> (resp/response text) 
    (resp/content-type "application/json") (resp/charset "utf-8")))

(defn- get-key [x]
  (keyword (string/replace x "$" "")))

(defn- get-val [x ret]
  (-> x get-key (#(%1 ret)) (#(if (string? %1) (str \" %1 \") %1)) str))

(defn- parse-template [tmt ret]
  (println tmt) (println ret)
  (string/replace tmt #"\$\S+\$" #(get-val %1 ret)))

(defn- create-html [ret template] 
  (-> (parse-template template ret) load-string page/html5))

(defn hiccup [ret template] 
  (-> (resp/response (create-html ret template)) 
    (resp/content-type "text/html")) (resp/charset"utf-8"))

(defn dispatch [ret template]
  (cond (:$r ret) ((:$r ret) ret template) 
        (:text ret) (text (:text ret))
        (:json ret) (json (:json ret))
        (:hiccup ret) (hiccup ret template)))

