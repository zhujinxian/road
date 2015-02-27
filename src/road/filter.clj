(ns road.filter
  (import (javax.servlet FilterConfig
                         FilterChain
                         ServletRequest
                         ServletResponse)
          (javax.servlet.http HttpServletRequest
                              HttpServletResponse))
  (:require [road.render :as render]
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
  (def handler (load-file (-> (.getServletContext conf)
    		   (.getRealPath "WEB-INF/classes/web.clj")))))

(defn -doFilter [this request response chain]
  (println "do filter")
  (if-let [ret (-> request 
                 (#(build-ring-req %1 context)) params/params-request handler)] 
    (let [ring-res (render/dispatch ret (get-template context ret))] (servlet/update-servlet-response response ring-res))
    (.doFilter chain request response))) 

(defn -destroy [this]
  (println "destroy filter"))


