(ns clojurewerkz.elephant.plans
  (:require [clojurewerkz.elephant.conversion :as cnv]
            [clojure.walk :as wlk])
  (:import [com.stripe.model Plan]
           [clojure.lang IPersistentMap])
  (:refer-clojure :exclude [list]))

;;
;; API
;;

(defn list
  ([m]
     (cnv/plans-coll->seq (Plan/all (wlk/stringify-keys m))))
  ([^String api-key m]
     (cnv/plans-coll->seq (Plan/all (wlk/stringify-keys m) api-key))))

(defn create
  [^IPersistentMap opts]
  (cnv/plan->map (Plan/create (wlk/stringify-keys opts))))

(defn retrieve
  ([^String id]
     (cnv/plan->map (Plan/retrieve id)))
  ([^String id ^String key]
     (cnv/plan->map (Plan/retrieve id key))))

(defn retrieve-or-create
  [^String id ^IPersistentMap opts]
  (try
    (retrieve id)
    (catch com.stripe.exception.InvalidRequestException e
      ;; :(
      ;; see https://github.com/stripe/stripe-java/issues/102
      (if (.startsWith (.getMessage e) "No such plan")
        (cnv/plan->map (Plan/create (merge {"id" id}
                                           (wlk/stringify-keys opts))))
        (throw e)))))

(defn update
  [^IPersistentMap plan ^IPersistentMap attrs]
  (if-let [o (:__origin__ plan)]
    (cnv/plan->map (.update o attrs))
       (throw (IllegalArgumentException.
               "plans/update only accepts maps returned by plans/create and plans/retrieve"))))
