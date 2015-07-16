(ns clojurewerkz.elephant.invoice-items
  (:refer-clojure :exclude [list])
  (:require [clojurewerkz.elephant.conversion :as cnv]
            [clojure.walk :as wlk])
  (:import [clojure.lang IPersistentMap]
           [com.stripe.model InvoiceItem]))

;;
;; API
;;

(defn ^IPersistentMap create
  [^IPersistentMap m]
  (cnv/invoice-item->map (InvoiceItem/create m)))

(defn ^IPersistentMap retrieve
  [^String id] 
  (cnv/invoice-item->map (InvoiceItem/retrieve id)))

(defn ^IPersistentMap update
  [^IPersistentMap m]
  (if-let [o (:__origin__ m)]
    (cnv/invoice-item->map (.update o))
    (throw (IllegalArgumentException.
            "invoice-item/update only accepts maps returned by invoice-item/create and invoice-item/retrieve"))))

(defn list
  ([]
     (list {}))
  ([m]
     (cnv/invoice-items-coll->seq (InvoiceItem/all (wlk/stringify-keys m))))
  ([^String api-key m]
     (cnv/invoice-items-coll->seq (InvoiceItem/all (wlk/stringify-keys m)) api-key)))

(defn ^IPersistentMap delete
  ([m]
     (if-let [o (:__origin__ m)]
       (cnv/deleted-invoice-item->map (.delete o))
       (throw (IllegalArgumentException.
               "invoice-item/delete only accepts maps returned by invoice-item/create, invoice-item/retrieve, and invoice-item/list"))))
  ([m ^String api-key]
     (if-let [o (:__origin__ m)]
       (cnv/deleted-invoice-item->map (.delete o api-key))
       (throw (IllegalArgumentException.
               "invoice-item/delete only accepts maps returned by invoice-item/create, invoice-item/retrieve, and invoice-item/list")))))
