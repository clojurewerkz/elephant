(ns clojurewerkz.elephant.balances
  "Operations on your Stripe balance"
  (:require [clojurewerkz.elephant.conversion :as cnv])
  (:import [com.stripe.model Balance BalanceTransaction]
           [java.util List]))


;;
;; API
;;

(defn retrieve
  ([]
     (cnv/balance->map (Balance/retrieve)))
  ([^String key]
     (cnv/balance->map (Balance/retrieve key))))

(defn ^List list-transactions
  ([]
     (cnv/balance-tx-coll->seq (BalanceTransaction/all nil)))
  ([m]
     (cnv/balance-tx-coll->seq (BalanceTransaction/all m)))
  ([m ^String key]
     (cnv/balance-tx-coll->seq (BalanceTransaction/all m key))))
