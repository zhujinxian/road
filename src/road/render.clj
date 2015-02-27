(ns road.render
  (:require [clojure.string :as string]
            [hiccup.page :as page]
            [ring.util.response :as resp]))

(defn text [text]
  (-> (resp/response text) 
    (#(resp/content-type %1 "text/plain")) (#(resp/charset %1 "utf-8"))))

(defn json [text]
  (-> (resp/response text) 
    (#(resp/content-type %1 "application/json")) (#(resp/charset %1 "utf-8"))))

(defn- get-key [x]
  (keyword (string/replace x "$" "")))

(defn- get-val [x ret]
  (-> x get-key (#(%1 ret)) (#(if (string? %1) (str \" %1 \") %1)) str))

(defn- parse-template [tmt ret]
  (println tmt) (println ret)
  (string/replace tmt #"\$\S+\$" #(get-val %1 ret)))

(defn- get-path-1 [context path]
  (.getRealPath context (str "WEB-INF/classes/" path)))

(defn- create-html [ret template] 
  (-> (parse-template template ret) load-string page/html5))

(defn hiccup [ret template] 
  (-> (resp/response (create-html ret template)) 
    (#(resp/content-type %1 "text/html")) (#(resp/charset %1 "utf-8"))))

(defn dispatch [ret template]
  (cond (:text ret) (text (:text ret))
        (:json ret) (json (:json ret))
        (:hiccup ret) (hiccup ret template)))
