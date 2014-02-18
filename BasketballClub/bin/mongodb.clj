(ns mongodb
  (:require [noir.session :as session]
            [clj-time.format :as time-format]
            [clj-time.core :as time])
  (:use [somnium.congomongo]))

(def conn 
  (make-connection "basketball_club"))

(set-connection! conn)

(defn- generate-id [collection]
  "Generate entity identifier." 
  (:seq (fetch-and-modify :sequences {:_id collection} {:$inc {:seq 1}}
                          :return-new? true :upsert? true)))

(defn- insert-entity [collection values]
   "Insert an entity into database."
  (insert! collection (assoc values :_id (generate-id collection))))

(defn insert-user
  [name email username password]
  "Insert user into database." 
  (insert-entity :users 
                  {:name name
                   :email email
                   :username username
                   :password password}))

(defn get-user-by-username [username]
  "Find user by username."  
  (fetch-one :users :where {:username username}))

(defn get-user-by-email [email]
  "Find user by email."  
  (fetch-one :users :where {:email email}))

(defn insert-user-player
  [player-name player-height player-position player-rating player-image date-added user-id]
  "Insert player into database." 
  (insert-entity :user-players 
                  {:player-name player-name
                   :player-height player-height
                   :player-position player-position
                   :player-rating player-rating
                   :player-image player-image
                   :date-added date-added
                   :user-id user-id}))

(defn get-starting-pg []
  "Return starting pg."
  (fetch :user-players :where {:player-position "PG"} :sort {:player-rating -1} :limit 1))

(defn get-starting-sg []
  "Return starting sg."
  (fetch :user-players :where {:player-position "SG"} :sort {:player-rating -1} :limit 1))

(defn get-starting-sf []
  "Return starting sf."
  (fetch :user-players :where {:player-position "SF"} :sort {:player-rating -1} :limit 1))

(defn get-starting-pf []
  "Return starting pf."
  (fetch :user-players :where {:player-position "PF"} :sort {:player-rating -1} :limit 1))

(defn get-starting-c []
  "Return starting c."
  (fetch :user-players :where {:player-position "C"} :sort {:player-rating -1} :limit 1))

(defn get-all-players []
  "Return all players from the database." 
  (fetch :user-players :sort {:date-added -1}))

(def parser-formatter (time-format/formatter "yyyy-MM-dd HH:mm:ss"))

(defn insert-new-player
  "Inserts data for new player into data base."
  [player-name player-height player-position player-rating player-image]
  (let [user (session/get :user)]
    (insert-entity :user-players
                   {:player-name player-name
                   :player-height player-height
                   :player-position player-position
                   :player-rating player-rating
                   :player-image player-image
                   :date-added (time-format/unparse parser-formatter (time/now))
                   :user-id (:_id user)})))