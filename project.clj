(defproject road "0.2.0-rc"
  :description "Road is a clojure web framework through packing java severlet filter."
  :url "https://github.com/zhujinxian/road"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [javax.servlet/servlet-api "2.5"]
                 [org.clojure/tools.macro "0.1.5"]
                 [ring "1.3.2"]
                 [clout "2.1.0"]
                 [hiccup "1.0.5"]
                 [org.clojure/tools.logging "0.3.1"]
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jdmk/jmxtools
                                                    com.sun.jmx/jmxri]]]
  :aot [road.core  road.filter road.router])
