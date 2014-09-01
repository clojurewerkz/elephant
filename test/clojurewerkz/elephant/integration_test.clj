(ns clojurewerkz.elephant.integration-test
    (:require [clojure.test :refer :all]
              [clojurewerkz.elephant.test-helpers :as th]
              [clojurewerkz.elephant.accounts :as ea]
              [clojurewerkz.elephant.balances :as eb]
              [clojurewerkz.elephant.charges  :as ech]))

(use-fixtures :each th/set-up-stripe-test-key)

(let [cc {"number"    "4242424242424242"
            "exp_month" 12
            "exp_year"  2015
            "cvc"       "123"
            "name"      "J Bindings Cardholder"
            "address_line1"   "140 2nd Street"
            "address_line2"   "4th Floor"
            "address_city"    "San Francisco"
            "address_zip"     "94105"
            "address_state"   "CA"
            "address_country" "USA"}
        ;; debit card
        dc  {"number"    "4000056655665556"
             "exp_month" 12
             "exp_year"  2015
             "cvc"       "123"
             "name"            "J Bindings Debitholder"
             "address_line1"   "140 2nd Street"
             "address_line2"   "4th Floor"
             "address_city"    "San Francisco"
             "address_zip"     "94105"
             "address_state"   "CA"
             "address_country" "USA"}
        ;; charge
        chg  {"amount"   100
              "currency" "usd"
              "card"     cc}
        ;; tokens
        cc-token {"card" cc}
        dc-token {"card" dc}
        customer {"card"        cc
                  "description" "J Bindings Customer"}
        plan     {"amount"         100
                  "currency"       "usd"
                  "interval"       "month"
                  "interval_count" 2
                  "name"           "J Bindings Plan"}
        coupon    {"duration"    "once"
                   "percent_off" 10}
        bank-acct {"country"        "US"
                   "routing_number" "110000000"
                   "account_number" "000123456789"}
        recipient {"name"   "J Test"
                   "type"   "individual"
                   "tax_id" "000000000"
                   "card"   dc
                   "bank_account" bank-acct}]
    (deftest test-account-retrieve
      (let [m (ea/retrieve)]
        (is (:id m))
        (is (= "usd" (:default-currency m)))
        (is (false? (:charge-enabled m)))
        (is (false? (:transfer-enabled m)))))

    (deftest test-balance-retrieve
      (let [m (eb/retrieve)]
        (is (not (:live-mode? m)))
        (is (:available m))
        (is (:pending m))))

    (deftest test-charge-create
      (let [m  (ech/create chg)
            cc (:card m)]
        (are [k v] (= (get m k) v)
             :amount 100
             :captured? true
             :paid? true)
        (are [k v] (= (get cc k) v)
             :brand "Visa"
             :customer nil
             :last-4-digits "4242")))

    (deftest test-charge-create-with-statement-description
      (let [s  "Elephant"
            m  (ech/create (merge chg {:description "integration tests"
                                       :statement_description s}))]
        (is (= s (:statement-description m)))))

    (deftest test-balance-transaction-retrieval
      (let [ch  (ech/create chg)
            txs (eb/list-transactions)
            tx1 (first txs)
            txs' (eb/list-transactions {"count" 2})]
        (is (not (empty? txs)))
        (is (:status tx1))
        (is (= 2 (count txs')))))

    (deftest test-charge-retrieve
      (let [x (ech/create chg)
            y (ech/retrieve (:id x))]
        (is (:id x))
        (is (:id y))
        (is (= (:id x) (:id y)))))

    (deftest test-charge-retrieve-with-nil-id
      (is (thrown? com.stripe.exception.InvalidRequestException
                   (ech/retrieve nil))))

    (deftest test-charge-refund
      (let [x  (ech/create chg)
            y  (ech/refund x)
            rs (:refunds y)]
        (is (:refunded? y))
        (is (= 1 (count rs)))
        (println rs)
        (is (= (-> rs first :charge) (:id y)))))

    (deftest test-charge-refund-update
      ))
