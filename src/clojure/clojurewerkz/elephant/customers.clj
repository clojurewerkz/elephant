(ns clojurewerkz.elephant.customers
  (:refer-clojure :exclude [list])
  (:require [clojurewerkz.elephant.conversion    :as cnv]
            [clojurewerkz.elephant.subscriptions :as sub]
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

(defn ^IPersistentMap retrieve-or-create
  [^String id ^IPersistentMap m]
  (try
    (retrieve id)
    (catch com.stripe.exception.InvalidRequestException ire
      (cnv/customer->map (Customer/create m)))))

(defn ^IPersistentMap subscribe
  [^IPersistentMap customer ^IPersistentMap subscription]
  (sub/create customer subscription))

(defn list
  ([]
     (list {}))
  ([m]
     (cnv/customers-coll->seq (Customer/all (wlk/stringify-keys m))))
  ([^String api-key m]
     (cnv/customers-coll->seq (Customer/all (wlk/stringify-keys m)) api-key)))

(defn ^IPersistentMap delete
  ([m]
     (if-let [o (:__origin__ m)]
       (cnv/deleted-customer->map (.delete o))
       (throw (IllegalArgumentException.
               "customers/delete only accepts maps returned by customers/create, charges/retrieve, and customers/list"))))
  ([m ^String api-key]
     (if-let [o (:__origin__ m)]
       (cnv/deleted-customer->map (.delete o api-key))
       (throw (IllegalArgumentException.
               "customers/delete only accepts maps returned by customers/create, charges/retrieve, and customers/list")))))
