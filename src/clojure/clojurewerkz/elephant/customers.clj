(ns clojurewerkz.elephant.customers
  (:require [clojurewerkz.elephant.conversion :as cnv])
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
