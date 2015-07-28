(ns clojurewerkz.elephant.cards
  (:require [clojurewerkz.elephant.conversion :as cnv])
  (:import [clojure.lang IPersistentMap])
  (:refer-clojure :exclude [list update]))

;;
;; API
;;

(defn create
  [^IPersistentMap customer ^IPersistentMap card]
  (if-let [o (:__origin__ customer)]
    (cnv/card->map (.createCard o {"source" (merge card {"object" "card"})}))
    (throw (IllegalArgumentException.
            "cards/create only accepts maps returned by customer/create and other library functions that return customer maps"))))

(defn create-from-token
  [^IPersistentMap customer ^String token]
  (if-let [o (:__origin__ customer)]
    (cnv/card->map (.createCard o token))
    (throw (IllegalArgumentException.
            "cards/create-from-token only accepts maps returned by customer/create and other library functions that return customer maps"))))

(defn retrieve
  [^IPersistentMap customer ^String card-id]
  (if-let [o (:__origin__ customer)]
    (cnv/card->map (let [cs (.getSources o)]
                     (.retrieve cs card-id)))
    (throw (IllegalArgumentException.
            "cards/retrieve only accepts maps returned by customer/create and other library functions that return customer maps"))))

(defn list
  [^IPersistentMap customer]
  (if-let [o (:__origin__ customer)]
    ;; TODO: pagination
    (map cnv/card->map (into [] (.. o getSources getData)))
    (throw (IllegalArgumentException.
            "cards/list only accepts maps returned by customer/create and other library functions that return customer maps"))))

(defn update
  [^IPersistentMap card ^IPersistentMap opts]
  (if-let [o (:__origin__ card)]
       (cnv/card->map (.update o opts))
       (throw (IllegalArgumentException.
               "cards/update only accepts maps returned by cards/create and other library functions that return card information"))))

(defn delete
  [^IPersistentMap card]
  (if-let [o (:__origin__ card)]
       (.delete o)
       (throw (IllegalArgumentException.
               "cards/dete only accepts maps returned by cards/create and other library functions that return card information"))))

