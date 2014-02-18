(ns player
  (:require [noir.session :as session]
            [ring.util.response :as response])
  (:use [template :only [get-template]]
        [hiccup.form :only [form-to label text-field submit-button]]
        [mongodb :only [insert-new-player]]))

(defn check-player-data
  "Checks player data."
  [player-name player-height player-position player-rating player-image]
  (cond
    (> 5 (.length player-name)) "Player name must be at least 5 character long."
    (> 1 (.length player-height)) "Player height must be at least 1 character long."
    (> 1 (.length player-position)) "Player position must be at least 1 character long."
    (> 1 (.length player-rating)) "Player rating must be at least 1 character long."
    (> 5 (.length player-image)) "Player image path must be at least 5 characters long."
    :else true))

(defn add-new-player
  "Adds new player."
  [player-name player-height player-position player-rating player-image]
  (let [player-error-message (check-player-data player-name player-height player-position player-rating player-image)]
    (if-not (string? player-error-message)
      (do 
        (insert-new-player player-name player-height player-position player-rating player-image)
        (response/redirect "/"))
      (do
        (session/flash-put! :player-error player-error-message)
        (session/flash-put! :player-name player-name)
        (session/flash-put! :player-height player-height)
        (session/flash-put! :player-position player-position)
        (session/flash-put! :player-rating player-rating)
        (session/flash-put! :player-image player-image)
        (response/redirect "/newplayer")))))
                
   
(defn player-page
  "Show player page"
  []
  (get-template "Player page"
   [:div.content
    [:p.playertitle "Enter  information about new player!"]
     [:p.playererror (session/flash-get :player-error)]
    (form-to [:post "/newplayer"]
             [:div.newplayerform
              [:div
              (label {:class "playerlabel"} :player-name "Player name")
               (text-field :player-name (session/flash-get :player-name))]
              [:div
               (label {:class "playerlabel"} :player-height "Height")
                (text-field :player-height (session/flash-get :player-height))]
              [:div
               (label {:class "playerlabel"} :player-position "Position")
                (text-field :player-position (session/flash-get :player-position))]
              [:div
               (label {:class "playerlabel"} :player-rating "Overall rating")
                (text-field :player-rating (session/flash-get :player-rating))]
              [:div
               (label {:class "playerlabel"} :player-image "Path to player image")
                (text-field :player-image (session/flash-get :player-image))]
               [:div
                (submit-button "Save player")]])]))
                
                                    
 
