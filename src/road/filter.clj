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

(defn -init [this conf]
  (load-file "web.clj"))

(defn -doFilter [this request response filter-chain]
  (println "do filter"))

(defn -destroy [this]
  (println "destroy filter"))

