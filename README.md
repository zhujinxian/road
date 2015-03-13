# Road

Road is a clojure web framework through packing java severlet filter or making a ring handler. 

## Package plugin

    [zhu/leiningen-war "0.0.1"]

## Usage

create a road project:

    $ lein new road web-test

## Run

run as ring handler in jetty adapter
  
    $ lein run

## package

package jar running standalone or as ring handler

    $ lein uberjar

package war running in web container:

    $ lein uberwar


## Example

project.clj as:

```clojure
(defproject web-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [road "0.2.0-rc"]]
  :plugins [[zhu/leiningen-war "0.0.1"]]
  :war {:webxml "web.xml"}
  :uberwar {:webxml "web.xml"}
  :main web-test.core
  :aot [web-test.core])
```

web.xml for package in war

web.xml

```xml
    <context-param>
        <param-name>ring-handler</param-name>
	<param-value>web-test.core/road</param-value>
    </context-param>

    <filter>
        <filter-name>RoadFilter</filter-name>
        <filter-class>road.Filter</filter-class>
    </filter> 
   
    <filter-mapping>
        <filter-name>RoadFilter</filter-name>
        <url-pattern>/*</url-pattern>
        <dispatcher>REQUEST</dispatcher>
        <dispatcher>FORWARD</dispatcher>
        <dispatcher>INCLUDE</dispatcher>
    </filter-mapping> 
```


web-test.core/core.clj


```clojure
(ns web-test.core (:gen-class)
  (:use [road.core])
  (:require [road.core :as road]
            [ring.middleware.params :as params]
            [ring.util.response :as resp]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [ring.adapter.jetty :as jetty]))

(defn render-test [ret tmt]
  (-> (resp/response "------render----test------") 
    (#(resp/content-type %1 "text/plain"))))

(defn foo
  "I don't do a whole lot."
  [x]
  (str "来自源码目录的参数：" x))

(defn handler [^Integer x]
    {:$r render-test :text (str "hello world, road goes sucess!" (foo x))})

(defn home [req content ^Integer num]
    {:hiccup "home.clj" :content (str "home" content) :num num})

(defroad road (GET "/web-test-0.1.0-SNAPSHOT-standalone/main" handler) 
              (GET "/web-test-0.1.0-SNAPSHOT-standalone/home/:num{\\d+}" home))

(defn -main [& args]
  (log/info "---------log4j test-------")
  (jetty/run-jetty road {:port 3000}))

```

resources/views/home.clj

```clojure
[:span {:class "foo"} "bar" $content$ $num$]
```


## The whole Example

<https://github.com/zhujinxian/web-test.git>

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
