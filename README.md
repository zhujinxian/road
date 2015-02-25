# Road

Road is a clojure web framework through packing java severlet filter. 

## Dependencies

    [zhu/clout "0.0.2"]
     
    
    [hiccup "1.0.5"]

## Package plugin

    [zhu/leiningen-war "0.0.1"]

## Usage

create a web project:

    $ lein new web-test

modify project.clj as:

```clojure
(defproject web-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [road "0.1.0-SNAPSHOT"]]
  :plugins [[zhu/leiningen-war "0.0.1"]]
  :war {:webxml "web.xml"}
  :uberwar {:webxml "web.xml"}
  :main web-test.core
  :aot [web-test.core])
```

create web.xml in resource directory, add road.filter to it.

web.xml

```xml
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


add web.clj to resources directory

web.clj

```clojure
(ns web.main
  (:use [road.router]))

(route-test "test web-test")

(defn handler [^Integer x]
  {:text (str "hello world, road goes sucess!" x)})

(defn home [req content ^Integer num]
  {:hiccup "home.clj" :content (str "home" content) :num num})

(defroutes app 
  (GET "/web-test-0.1.0-SNAPSHOT-standalone" handler)
  (GET "/home/:num{\\d+}" home))
```

**add home.clj to resources directory

home.clj (clojure hiccup template)

```clojure
[:span {:class "foo"} "bar" $content$ $num$]
```


package war:

    $ lein uberwar

## Example

<https://github.com/zhujinxian/web-test.git>

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
