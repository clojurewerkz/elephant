(ns clojurewerkz.elephant.integration-test
    (:require [clojure.test :refer :all]
              [clojurewerkz.elephant.test-helpers  :as th]
              [clojurewerkz.elephant.accounts      :as ea]
              [clojurewerkz.elephant.balances      :as eb]
              [clojurewerkz.elephant.charges       :as ech]
              [clojurewerkz.elephant.cards         :as ecc]
              [clojurewerkz.elephant.customers     :as ecr]
              [clojurewerkz.elephant.plans         :as ep]
              [clojurewerkz.elephant.subscriptions :as esub])
    (:import java.util.UUID
             com.stripe.exception.InvalidRequestException))

(use-fixtures :each th/set-up-stripe-test-key)

(defn unique-plan
  [m]
  (merge m {"id" (format "MY-CLJ-PLAN-%s" (str (UUID/randomUUID)))}))

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
    (defn delete-all-customers
      []
      (dotimes [i 3]
        (doseq [c (ecr/list {"limit" 100})]
          (try
            (ecr/delete c)
            (catch InvalidRequestException ire)))))

    (deftest test-account-retrieve
      (let [m (ea/retrieve)]
        (is (:id m))
        (is (= "usd" (:default-currency m)))
        (is (false? (:charges-enabled m)))
        (is (false? (:transfers-enabled m)))))

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
        (is (= (-> rs first :charge) (:id y)))))

    (deftest test-charge-partial-refund
      (let [n  50
            x  (ech/create chg)
            y  (ech/refund x {"amount" n})]
        (is (not (:refunded? y)))
        (is (= n (:amount-refunded y)))))

    (deftest test-charge-capture
      (let [x (ech/create (merge chg {"capture" false}))
            y (ech/capture x)]
        (is (false? (:captured? x)))
        (is (:captured? y))))

    (deftest test-charge-with-invalid-card
      (let [m (assoc chg "card" {"number" "4242424242424241"
                                 "exp_month" 12
                                 "exp_year"  2015})]
        (is (thrown? com.stripe.exception.CardException
                     (ech/create m)))))

    (deftest test-customer-create
      (let [c (ecr/create customer)]
        (is (= "J Bindings Customer" (:description c)))))

    (deftest test-customer-create-without-card
      (let [s "J Bindings Customer 2"
            c (ecr/create {"description" s})]
        (is (= s (:description c)))))

    (deftest test-customer-create-with-duplicate-id
      (let [s "J Bindings Customer 2"
            c (ecr/create {"description" s})]
        (is (thrown? com.stripe.exception.InvalidRequestException
                     (ecr/create {"description" s "id" (:id c)})))))

    (deftest test-customer-find-or-create
      (let [c1 (ecr/create customer)
            id (:id c1)
            c2 (ecr/retrieve-or-create id customer)
            c3 (ecr/retrieve-or-create (str (UUID/randomUUID)) customer)]
        (is (= id (:id c2)))
        (is (not (= id (:id c3))))))

    (deftest test-customer-retrieve
      (let [x (ecr/create customer)
            y (ecr/retrieve (:id x))]
        (is (:id y))
        (is (= (:id x) (:id y)))
        (is (= (:created x) (:created y)))))

    (deftest test-customer-card-update
      (let [x (ecr/create customer)
            s "J Bindings Cardholder, Jr."
            m (-> x :cards first)
            c (ecc/update m {"name" s})]
        (is (= (:name c) s))
        (is (= (:id c) (:id m)))))

    (deftest test-customer-list
      (delete-all-customers)
      (is (zero? (count (ecr/list))))
      (let [x  (ecr/create customer)
            xs (ecr/list)]
        (is (sequential? xs))
        (is (= #{(:id x)} (set (map :id xs))))))

    (deftest test-plan-create
      (let [x (ep/create (unique-plan plan))]
        (is (= 2 (:interval-count x)))
        (is (= "month" (:interval x)))))

    (deftest test-idempotent-plan-create
      (let [x1 (ep/retrieve-or-create "CLJW-IDEMPOTENCE-PLAN" plan)
            x2 (ep/retrieve-or-create "CLJW-IDEMPOTENCE-PLAN" plan)]
        (is (= (:id x1) (:id x2)))
        (is (= 2 (:interval-count x1) (:interval-count x2)))))

    (deftest test-plan-create-with-statement-description
      (let [s "ClojureWerkz"
            x (ep/create (merge (unique-plan plan) {"statement_description" s}))]
        (is (= s (:statement-description x)))))

    (deftest test-plan-update
      (let [s "New Plan Name"
            x (ep/create (unique-plan plan))
            y (ep/update x {"name" s})
            z (ep/retrieve (:id y))]
        (is (= (:name y) (:name z) s))))

    (deftest test-plan-list
      (let [x  (ep/create (unique-plan plan))
            xs (ep/list {"count" 1})]
        (is (= 1 (count xs)))))

    (deftest test-create-customer-with-plan
      (let [p (ep/create (unique-plan plan))
            c (ecr/create customer)
            x (ecr/subscribe c {"plan" (:id p)})]
        (is (= (:id p) (get-in x [:plan :id])))))

    (deftest test-update-subscription
      (let [p1 (ep/create (unique-plan plan))
            p2 (ep/create (unique-plan plan))
            c  (ecr/create customer)
            x  (ecr/subscribe c {"plan" (:id p1)})
            y  (esub/update x {"plan" (:id p2)})]
        (is (= (:id p2) (get-in y [:plan :id])))))

    (deftest test-cancel-subscription
      (let [p (ep/create (unique-plan plan))
            c (ecr/create customer)
            x (ecr/subscribe c {"plan" (:id p)})
            y (esub/cancel x)]
        (is (:cancelled-at y))))

    (deftest test-list-subscriptions
      (let [p  (ep/create (unique-plan plan))
            c  (ecr/create customer)
            x  (esub/create c {"plan" (:id p)})
            xs (esub/list c)]
        (is (= 1 (count xs)))
        (is (set (map :id xs)) (:id x)))))
