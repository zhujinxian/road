(ns road.filter
  (:require [road.render :as render]
            [clojure.string :as str]
            [ring.util.servlet :as servlet]
            [ring.middleware.params :as params]))

(gen-class
      :name road.Filter
      :implements [javax.servlet.Filter]
      :prefix "-"
      :main false)

(def context)

(def handler)

(defn build-ring-req [req ctx]
  (-> req servlet/build-request-map 
    (#(merge %1 {:servlet-request req :servlet-context ctx :servlet-context-path (.getContextPath req)}))))

(defn get-template [ctx ret] 
  (if-let [fname (:hiccup ret)] 
    (-> (.getRealPath ctx (str "WEB-INF/classes/views/" fname)) slurp))) 

(defn -init [this conf]
  (def context (.getServletContext conf))
  (if-let [ring-handler-name (.getInitParameter context "ring-handler")] 
    (def handler (load-string 
                   (str "(require '[" (first (str/split ring-handler-name #"/"))  "]) " ring-handler-name))))) 

(defn -doFilter [this request response chain]
  (println "do filter")
  (if-let [ret (-> request 
                 (#(build-ring-req %1 context)) handler)] (servlet/update-servlet-response response ret) 
    (.doFilter chain request response))) 

(defn -destroy [this]
  (println "destroy filter"))


