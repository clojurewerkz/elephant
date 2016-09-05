(ns clojurewerkz.elephant.balances
  "Operations on your Stripe balance"
  (:require [clojurewerkz.elephant.conversion :as cnv]
            [clojurewerkz.elephant.util :refer (api-key->request-options)])
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
     (cnv/balance-tx-coll->seq (BalanceTransaction/list nil)))
  ([m]
     (cnv/balance-tx-coll->seq (BalanceTransaction/list m)))
  ([^String api-key m]
     (cnv/balance-tx-coll->seq (BalanceTransaction/list m (api-key->request-options api-key)))))
