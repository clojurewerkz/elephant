(ns clojurewerkz.elephant.coupons
  (:refer-clojure :exclude [list update])
  (:require [clojurewerkz.elephant.conversion :as cnv]
            [clojurewerkz.elephant.util :refer (api-key->request-options)]
            [clojure.walk :as wlk])
  (:import [clojure.lang IPersistentMap]
           [com.stripe.model Coupon]))

;;
;; API
;;

(defn ^IPersistentMap create
  [^IPersistentMap m]
  (cnv/coupon->map (Coupon/create m)))

(defn ^IPersistentMap retrieve
  [^String id]
  (cnv/coupon->map (Coupon/retrieve id)))

(defn ^IPersistentMap update
  [^IPersistentMap m]
  (if-let [o (:__origin__ m)]
    (cnv/coupon->map (.update o))
    (throw (IllegalArgumentException.
            "coupon/update only accepts maps returned by coupons/create and coupons/retrieve"))))

(defn list
  ([]
     (list {}))
  ([m]
     (cnv/coupons-coll->seq (Coupon/list (wlk/stringify-keys m))))
  ([^String api-key m]
     (cnv/coupons-coll->seq (Coupon/list (wlk/stringify-keys m) (api-key->request-options api-key)))))

(defn ^IPersistentMap delete
  ([m]
     (if-let [o (:__origin__ m)]
       (cnv/deleted-coupon->map (.delete o))
       (throw (IllegalArgumentException.
               "coupons/delete only accepts maps returned by coupons/create, coupons/retrieve, and coupons/list"))))
  ([m ^String api-key]
     (if-let [o (:__origin__ m)]
       (cnv/deleted-coupon->map (.delete o api-key))
       (throw (IllegalArgumentException.
               "coupons/delete only accepts maps returned by coupons/create, coupons/retrieve, and coupons/list")))))
