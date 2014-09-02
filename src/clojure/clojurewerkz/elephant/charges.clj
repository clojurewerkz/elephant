(ns clojurewerkz.elephant.charges
  "Operations on charges"
  (:require [clojurewerkz.elephant.conversion :as cnv]
            [clojure.walk :as wlk])
  (:import [com.stripe.model Charge]
           [clojure.lang IPersistentMap]))

;;
;; API
;;

#_ (defn list
  ([m]
     (cnv/stripe-collection->seq (Charge/all (wlk/stringify-keys m))))
  ([^String api-key m]
     (cnv/stripe-collection->seq (Charge/all (wlk/stringify-keys m) api-key))))

(defn create
  ([m]
     (cnv/charge->map (Charge/create (wlk/stringify-keys m))))
  ([^String api-key m]
     (cnv/charge->map (Charge/create (wlk/stringify-keys m) api-key))))

(defn retrieve
  ([^String id]
     (cnv/charge->map (Charge/retrieve id)))
  ([^String id ^String key]
     (cnv/charge->map (Charge/retrieve id key))))

(defn refund
  ([^IPersistentMap charge]
     (refund charge {}))
  ([^IPersistentMap charge ^IPersistentMap opts]
     (if-let [o (:__origin__ charge)]
       (cnv/charge->map (.refund o opts))
       (throw (IllegalArgumentException.
               "charges/refund only accepts maps returned by charges/create and charges/retrieved")))))

(defn capture
  [^IPersistentMap m]
  (if-let [o (:__origin__ m)]
    (cnv/charge->map (.capture o))
    (throw (IllegalArgumentException.
            "charges/capture only accepts maps returned by charges/create and charges/retrieved"))))
