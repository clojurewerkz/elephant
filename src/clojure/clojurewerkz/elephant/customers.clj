(ns clojurewerkz.elephant.customers
  (:require [clojurewerkz.elephant.conversion :as cnv]
            [clojure.walk :as wlk])
  (:import [clojure.lang IPersistentMap]
           [com.stripe.model Customer]))

;;
;; API
;;

(defn ^IPersistentMap create
  [^IPersistentMap m]
  (cnv/customer->map (Customer/create m)))

(defn ^IPersistentMap retrieve
  [^String id]
  (cnv/customer->map (Customer/retrieve id)))

(defn ^IPersistentMap subscribe
  [^IPersistentMap customer ^IPersistentMap subscription]
  (if-let [o (:__origin__ customer)]
    (cnv/subscription->map (.createSubscription o (wlk/stringify-keys subscription)))
    (throw (IllegalArgumentException.
            "customers/subscribe only accepts maps returned by customers/create and customers/retrieve"))))

(defn ^IPersistentMap update-subscription
  [^IPersistentMap customer ^IPersistentMap subscription]
  (if-let [o (:__origin__ customer)]
    (cnv/subscription->map (.updateSubscription o (wlk/stringify-keys subscription)))
    (throw (IllegalArgumentException.
            "customers/update-subscription only accepts maps returned by customers/create and customers/retrieve"))))
