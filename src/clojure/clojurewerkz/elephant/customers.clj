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
