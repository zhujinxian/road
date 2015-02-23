# Road

Road is a clojure web framework for packing java severlet filter. 

## Usage

web.clj

(ns web.main
  (:use [road.router]))

(route-test "test web-test")

(defn open-home [^Integer x ^Float y des]
  (str "hello world, road goes sucess!" (* x y) des))

(defn home [req ^Integer test]
  (str "home test = " test))

(defroutes app 
  (GET "/web-test-0.1.0-SNAPSHOT-standalone" open-home)
  (GET "/home/test/hello/:test" home))


## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
