(ns clojurewerkz.elephant.customers
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
