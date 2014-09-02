(ns clojurewerkz.elephant.cards
  (:require [clojurewerkz.elephant.conversion :as cnv])
  (:import [clojure.lang IPersistentMap]))

;;
;; API
;;

(defn update
  [^IPersistentMap card ^IPersistentMap opts]
  (if-let [o (:__origin__ card)]
       (cnv/card->map (.update o opts))
       (throw (IllegalArgumentException.
               "cards/update only accepts maps returned by cards/create and other library functions that return card information"))))
