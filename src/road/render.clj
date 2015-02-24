(ns road.render
  (:require [clojure.string :as string]
            [hiccup.page :as page]))

(defn text [res text]
  (do (.setContentType res "text/plain") (-> res .getWriter (.print text))))

(defn json [res text]
  (do (.setContentType res "application/json") (-> res .getWriter (.print text))))

(defn- get-key [x]
  (keyword (string/replace x "$" "")))

(defn- get-val [x ret]
  (-> x get-key (#(%1 ret)) (#(if (string? %1) (str \" %1 \") %1)) str))

(defn- parse-template [tmt ret]
  (println tmt) (println ret)
  (string/replace tmt #"\$\S+\$" #(get-val %1 ret)))

(defn- get-path [context path]
  (.getRealPath context (str "WEB-INF/classes/" path)))

(defn- create-html [context ret]
  (println (-> ret :hiccup (#(get-path context %1))))
  (let [tmt (-> ret :hiccup (#(get-path context %1)) slurp)]
    (-> (parse-template tmt ret) load-string page/html5)))

(defn hiccup [context res ret]
  (do (.setContentType res "text/html") (-> res .getWriter (.print (create-html context ret)))))

(defn dispatch [context res ret]
  (cond (:text ret) (text res (:text ret))
        (:json ret) (json res (:json ret))
        (:hiccup ret) (hiccup context res ret)))
