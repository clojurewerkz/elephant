(ns clojurewerkz.elephant.invoices
  (:refer-clojure :exclude [list update])
  (:require [clojurewerkz.elephant.conversion :as cnv]
            [clojurewerkz.elephant.util :refer (api-key->request-options)]
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

(defn ^IPersistentMap upcoming
  [^IPersistentMap m]
  (cnv/invoice->map (Invoice/upcoming m)))

(defn ^boolean prorated?
  "True if at least one item and all items are prorated."
  [^IPersistentMap invoice]
  (let [items (:invoice-items invoice)]
    (and (-> items count pos?)
         (every? :proration items))))

(defn list
  ([]
     (list {}))
  ([m]
     (cnv/invoice-coll->seq (Invoice/list (wlk/stringify-keys m))))
  ([^String api-key m]
     (cnv/invoice-coll->seq (Invoice/list (wlk/stringify-keys m) (api-key->request-options api-key)))))
