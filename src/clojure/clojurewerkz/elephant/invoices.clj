(ns clojurewerkz.elephant.invoices
  (:refer-clojure :exclude [list update])
  (:require [clojurewerkz.elephant.conversion :as cnv]
            [clojure.walk :as wlk])
  (:import [clojure.lang IPersistentMap]
           [com.stripe.model Invoice]))

;;
;; API
;;

(defn ^IPersistentMap create
  [^IPersistentMap m]
  (cnv/invoice->map (Invoice/create m)))

(defn ^IPersistentMap retrieve
  [^String id] 
  (cnv/invoice->map (Invoice/retrieve id)))

(defn ^IPersistentMap update
  [^IPersistentMap m]
  (if-let [o (:__origin__ m)]
    (cnv/invoice->map (.update o))
    (throw (IllegalArgumentException.
            "invoice/update only accepts maps returned by invoice/create and invoice/retrieve"))))

(defn list
  ([]
     (list {}))
  ([m]
     (cnv/invoice-coll->seq (Invoice/all (wlk/stringify-keys m))))
  ([^String api-key m]
     (cnv/invoice-coll->seq (Invoice/all (wlk/stringify-keys m)) api-key)))
