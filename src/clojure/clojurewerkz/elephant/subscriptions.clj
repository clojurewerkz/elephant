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

(defn ^IPersistentMap cancel
  ([^IPersistentMap subscription]
     (cancel subscription nil))
  ([^IPersistentMap subscription ^IPersistentMap opts]
     (if-let [o (:__origin__ subscription)]
       (cnv/subscription->map (.cancel o (wlk/stringify-keys opts)))
       (throw (IllegalArgumentException.
               "subscriptions/cancel only accepts maps returned by customers/subscribe and subscriptions/retrieve")))))
