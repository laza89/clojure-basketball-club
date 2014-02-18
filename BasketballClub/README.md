# BasketballClub

This is a web application in Clojure wich uses libraries: Ring, Mongo-session, Compojure, lib-noir, Hiccup, clj-time, CongoMongo. In this application you can view basketball club players. The system recommends the best starting five (point guard-shooting guard-small forward-power forward-center) based on the overall player ratings. You can log in with user "admin" and password "admin". Logged user can add new players. You can also register a new account.

## Usage

Download and install Leiningen. http://leiningen-win-installer.djpowell.net/

Download and install MongoDB. http://www.mongodb.org/downloads

Start MongoDB.

Start your application - cd to project location - lein run

##References

Clojure programming, Chas Emerick, Brian Carper and Cristophe Grand

Developing and Deploying a Simple Clojure Web Application and A brief overview of the Clojure web stack for learning Ring, Compojure and Hiccup

Programming Collective Intelligence, Toby Seagaran

CongoMongo library for using MongoDB with Clojure - mongo.clj

Hickory library for parsing HTML used to extract data from web page - extract_data.clj.

## License

Distributed under the Eclipse Public License, the same as Clojure.
