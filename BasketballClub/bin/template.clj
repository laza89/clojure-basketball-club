(ns template
  (:require [noir.session :as session])
  (:use  [hiccup.core :only [html]]
         [hiccup.page :only [include-css doctype]]
         [mongodb :only [get-starting-pg get-starting-sg get-starting-sf get-starting-pf get-starting-c]]))

(defn not-logged-in-menu
  "Generates the menu for all pages when the user is not logged in."
  []
  [:ul#menu
   [:li#home
    [:a {:href "/"} "Home"]]
   [:li#logreg
    [:a {:href "/login"} "Login"]]
   [:li#logreg 
    [:a {:href "/register"} "Register"]]])

(defn logged-in-menu
  "Generates the menu for all pages when the user is logged in."
  []
  [:ul#menu 
   [:li#home
    [:a {:href "/"} "Home"]]
   [:li#logreg 
    [:a {:href "/newplayer"} "New player"]]
   [:li#logreg
    [:a {:href "/logout"} "Logout"]]])

(defn show-one-player
  "Generates HTML for one player."
  [player]
  [:li
    [:h2.position (:player-position player)]
    [:img {:class "playerimage" :src  (:player-image player)}]
    [:h2 (:player-name player)]
    [:h2 (:player-rating player)]])
  
(defn get-starting-fives
  "Retrieves the starting five players and displays them."
  []
  [:ul
   (let [pgs (get-starting-pg)]
   (for [pg pgs]
		(show-one-player pg)))
   (let [sgs (get-starting-sg)]
   (for [sg sgs]
		(show-one-player sg)))
   (let [sfs (get-starting-sf)]
   (for [sf sfs]
		(show-one-player sf)))
   (let [pfs (get-starting-pf)]
   (for [pf pfs]
		(show-one-player pf)))
   (let [cs (get-starting-c)]
   (for [c cs]
		(show-one-player c)))])

(defn get-template
  "Generates template for all pages in application."
  [title content]
  (html
    (doctype :xhtml-transitional)
    [:html {:xmlns "http://www.w3.org/1999/xhtml" "xml:lang" "en" :lang "en"} 
      [:head
        (include-css "/css/style.css")
        [:meta {:charset "UTF-8"}]
        [:title title]]
      [:body
       (let [user (session/get :user)] 
         (if-not user (not-logged-in-menu) (logged-in-menu)))
         [:div#container
          [:div#titlelogo
	          [:img {:class "logo left" :src "../images/logo.jpg"}]
	          [:h1#title "Basketball Club"]
	          [:img {:class "logo right" :src "../images/logo.jpg"}]]
          [:div#starting
           [:h1 "Recommended starting five"]
           (get-starting-fives)]
          content]
          [:div#footer "Lazar Stanojlović 2014, FON"]]]))
