(ns clojurewerkz.elephant.subscriptions
  (:require [clojurewerkz.elephant.conversion :as cnv]
            [clojure.walk :as wlk])
  (:import [clojure.lang IPersistentMap]
           [com.stripe.model Subscription]))

;;
;; API
;;

(defn ^IPersistentMap update
  [^IPersistentMap subscription ^IPersistentMap attrs]
  (if-let [o (:__origin__ subscription)]
    (cnv/subscription->map (.update o (wlk/stringify-keys attrs)))
    (throw (IllegalArgumentException.
            "subscriptions/update only accepts maps returned by customers/subscribe and subscriptions/retrieve"))))
