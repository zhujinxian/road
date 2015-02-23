(ns road.filter
  (import (javax.servlet FilterConfig
                         FilterChain
                         ServletRequest
                         ServletResponse)
          (javax.servlet.http HttpServletRequest
                              HttpServletResponse)))

(gen-class
      :name zhu.road.Filter
      :implements [javax.servlet.Filter]
      :prefix "-"
      :main false)

(def app)

(defn -init [this conf]
  (def app (load-file (-> (.getServletContext conf)
    		   (.getRealPath "WEB-INF/classes/web.clj")))))

(defn -doFilter [this request response filter-chain]
  (println "do filter")
  (let [ret (app request)] 
    (if (nil? ret) 
          ((.setStatus response 404) (.write (.getWriter response) "page not found")) 
          ((.setStatus response 200) (.write (.getWriter response) ret)))))

(defn -destroy [this]
  (println "destroy filter"))


