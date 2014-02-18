(ns server
  (:require [compojure.route :as route]
            [noir.session :as session]
				    [ring.util.response :as response])
  (:use [compojure.core :only [defroutes GET POST DELETE PUT]]
        [ring.adapter.jetty :only [run-jetty]]
        [main :only [main-page]]
        [login :only [login-page do-login do-logout]]
        [register :only [register-page do-register]]
        [mongodb :only [insert-user insert-user-player get-user-by-username]]
        [player :only [player-page add-new-player]]
        [ring.middleware.reload :only [wrap-reload]]
        [ring.middleware.stacktrace :only [wrap-stacktrace]]
        [ring.middleware.params :only [wrap-params]]))

(defroutes handler
  (GET "/" [] (main-page))
  (GET "/login" [] (let [user (session/get :user)] (if-not user (login-page) (main-page))))
  (POST "/login" [username password] (do-login username password))
  (GET "/logout" [] (do (do-logout) (response/redirect "/")))
  (GET "/register" [] (let [user (session/get :user)] (if-not user (register-page) (main-page))))
  (POST "/register" [first-name last-name email username password confirm-password] (do-register first-name last-name email username password confirm-password)) 
  (GET "/newplayer" [] (let [user (session/get :user)] (if user (player-page) (login-page))))
  (POST "/newplayer" [player-name player-height player-position player-rating player-image] (add-new-player player-name player-height player-position player-rating player-image))
  (route/resources "/")
  (route/not-found "Page not found."))

 (def app
  (-> #'handler
    (wrap-reload)
    (wrap-params)
    (session/wrap-noir-flash)
    (session/wrap-noir-session)
    (wrap-stacktrace)))


 (defn start-jetty-server []
   (run-jetty #'app {:port 8080 :join? false})
   (println "\nWelcome to the Basketball Club web application. Browse to http://localhost:8080 in your browser to get started!"))
 
 (defn insert-admin [] 
  (insert-user "Administrator" "admin@admin.com" "admin" "admin"))
 
 (defn insert-test-data [] 
   (let [user (get-user-by-username "admin")]
     (do
       (insert-user-player "Tracy McGrady" "2.03m" "SF" "9.9" "images/mcgrady.jpg" "2014-02-17" (:_id user))
       (insert-user-player "Koby Bryant" "1.98m" "SG" "9.7" "images/bryant.jpg" "2014-02-17" (:_id user))
       (insert-user-player "Shaquille O`Neal" "2.17m" "C" "9.8" "images/oneal.jpg" "2014-02-17" (:_id user))
       (insert-user-player "Chris Paul" "1.83m" "PG" "8" "images/paul.jpg" "2014-02-17" (:_id user))
       (insert-user-player "Kevin Garnett" "2.14m" "PF" "8.7" "images/garnett.jpg" "2014-02-17" (:_id user))
       (insert-user-player "Tim Duncan" "2.13m" "PF" "9.3" "images/duncan.jpg" "2014-02-17" (:_id user))
       (insert-user-player "Alen Iverson" "1.83m" "PG" "8.7" "images/iverson.jpg" "2014-02-17" (:_id user))
       (insert-user-player "Paul George" "2.04m" "SG" "8.4" "images/george.jpg" "2014-02-17" (:_id user))
       (insert-user-player "LeBron James" "2.00m" "SF" "9.8" "images/james.jpg" "2014-02-17" (:_id user))
       (insert-user-player "Kyrie Irving" "1.94m" "PG" "9.3" "images/irving.jpg" "2014-02-17" (:_id user)))))

 (defn -main [& args]
   (do
     (start-jetty-server)
      (let [user (get-user-by-username "admin")]
       (if-not user (do (insert-admin) (insert-test-data))))))
 