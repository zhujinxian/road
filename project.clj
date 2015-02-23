(defproject road "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [javax.servlet/servlet-api "2.5"]
                 [org.clojure/tools.macro "0.1.5"]
                 [zhu.road/clout "2.1.0"]]
  :aot [road.core  road.filter road.router])
