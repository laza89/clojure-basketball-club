(ns main
  (:use  [template :only [get-template]]
         [mongodb :only [get-all-players]]))

(defn show-one-player
  "Generates HTML for one player."
  [player]
  [:div.playerinfo
   [:img {:src (:player-image player)}]
    [:h2 (:player-name player)]
    [:p.playerheight (str "Height: "(:player-height player))]
    [:p.playerposition (str "Position: "(:player-position player))]
     [:p.playerrating (str "Overall rating: "(:player-rating player))]
    ])
  
(defn show-all-players
  "Retrieves all players from database and displays them."
  []
  [:div.playerinfo
   (let [players (get-all-players)]
   (for [player players]
		(show-one-player player)))])

(defn main-page 
  "Displays main page."
  []
  (get-template "Basketball Club" 
   [:div.content
    (show-all-players)
     ]))

