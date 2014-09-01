(ns clojurewerkz.elephant.accounts
  "Operations on your Stripe account"
  (:require [clojurewerkz.elephant.conversion :as cnv])
  (:import [com.stripe.model Account]))


;;
;; API
;;

(defn retrieve
  ([]
     (cnv/account->map (Account/retrieve)))
  ([^String key]
     (cnv/account->map (Account/retrieve key))))
