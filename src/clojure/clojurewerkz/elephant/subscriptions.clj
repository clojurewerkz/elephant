(ns clojurewerkz.elephant.subscriptions
  (:require [clojurewerkz.elephant.conversion :as cnv]
            [clojure.walk :as wlk])
  (:import [clojure.lang IPersistentMap]
           [com.stripe.model Subscription])
  (:refer-clojure :exclude [list update]))

;;
;; API
;;

(defn list
  ([^IPersistentMap customer]
     (list customer {}))
  ([^IPersistentMap customer ^IPersistentMap opts]
     (if-let [o (:__origin__ customer)]
       (cnv/subscriptions-coll->seq (-> o .getSubscriptions (.all (wlk/stringify-keys opts))))
       (throw (IllegalArgumentException.
               "subscriptions/list only accepts maps returned by customers/create and customers/retrieve")))))

#_ (defn ^IPersistentMap retrieve
  [^IPersistentMap customer ^String id]
  (if-let [o (:__origin__ customer)]
    (cnv/subscription->map (.createSubscription o (wlk/stringify-keys subscription)))
    (throw (IllegalArgumentException.
            "subscriptions/create only accepts maps returned by customers/create and customers/retrieve"))))

(defn ^IPersistentMap create
  [^IPersistentMap customer ^IPersistentMap subscription]
  (if-let [o (:__origin__ customer)]
    (cnv/subscription->map (.createSubscription o (wlk/stringify-keys subscription)))
    (throw (IllegalArgumentException.
            "subscriptions/create only accepts maps returned by customers/create and customers/retrieve"))))

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
