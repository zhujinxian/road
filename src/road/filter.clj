(ns road.filter
  (import (javax.servlet FilterConfig
                         FilterChain
                         ServletRequest
                         ServletResponse)
          (javax.servlet.http HttpServletRequest
                              HttpServletResponse))
  (:require [road.render :as render]))

(gen-class
      :name zhu.road.Filter
      :implements [javax.servlet.Filter]
      :prefix "-"
      :main false)

(def context)

(def app)

(defn -init [this conf]
  (def context (.getServletContext conf)) 
  (def app (load-file (-> (.getServletContext conf)
    		   (.getRealPath "WEB-INF/classes/web.clj")))))

(defn -doFilter [this request response chain]
  (println "do filter")
  (let [ret (app request)] 
    (if (nil? ret) 
          (.doFilter chain request response)
          (render/dispatch context response ret)))) 
          ;(do (.setContentType response "text/plain") (.print (.getWriter response) ret)))))

(defn -destroy [this]
  (println "destroy filter"))


