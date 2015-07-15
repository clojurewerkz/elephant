(ns clojurewerkz.elephant.conversion
  "Internal Java object => persistent Clojure data structure conversion functions.
   Not supposed to be used directly, not a part of the public Elephant API."
  (:import [clojure.lang IPersistentMap]
           java.util.List
           [com.stripe.model StripeCollection StripeCollectionAPIResource
            Account Balance BalanceTransaction Card Charge Coupon Customer Dispute
            Discount Fee Money NextRecurringCharge Subscription Refund
            DeletedCustomer DeletedCoupon]))


;;
;; API
;;

(defn ^IPersistentMap account->map
  [^Account acc]
  {:id    (.getId acc)
   :email (.getEmail acc)
   :currencies-supported (vec (.getCurrenciesSupported acc))
   :charges-enabled      (or (.getChargesEnabled acc) false)
   :transfers-enabled    (or (.getTransfersEnabled acc) false)
   :details-submitted    (.getDetailsSubmitted acc)
   :statement-descriptor (.getStatementDescriptor acc)
   :default-currency     (.getDefaultCurrency acc)
   :country              (.getCountry acc)
   :timezone             (.getTimezone acc)
   :display-name         (.getDisplayName acc)
   :__origin__           acc})

(defn ^IPersistentMap money->map
  [^Money m]
  {:amount   (.getAmount m)
   :currency (.getCurrency m)
   :__origin__ m})

(defn ^IPersistentMap balance->map
  [^Balance b]
  {:available  (map money->map (.getAvailable b))
   :pending    (map money->map (.getPending b))
   :live-mode? (.getLivemode b)
   :__origin__ b})

(defn ^IPersistentMap card->map
  [^Card c]
  {:id               (.getId c)
   :expiration-month (.getExpMonth c)
   :expiration-year  (.getExpYear c)
   :last-4-digits    (.getLast4 c)
   :country          (.getCountry c)
   :type             (.getType c)
   :name             (.getName c)
   :customer         (.getCustomer c)
   :recipient        (.getRecipient c)
   :address {:line1    (.getAddressLine1 c)
             :line2    (.getAddressLine2 c)
             :zip      (.getAddressZip c)
             :city     (.getAddressCity c)
             :state    (.getAddressState c)
             :country  (.getAddressCountry c)
             :zip-check   (.getAddressZipCheck c)
             :line1-check (.getAddressLine1Check c)}
   :cvc-check   (.getCvcCheck c)
   :fingerprint (.getFingerprint c)
   :brand       (.getBrand c)
   :funding     (.getFunding c)
   :__origin__  c})

(defn ^IPersistentMap fee->map
  [^Fee fe]
  {:type        (.getType fe)
   :application (.getApplication fe)
   :amount      (.getAmount fe)
   :description (.getDescription fe)
   :currency    (.getCurrency fe)
   :__origin__ fe})

(defn ^List fees->seq
  [^List xs]
  (map fee->map xs))

(defn ^IPersistentMap balance-tx->map
  [^BalanceTransaction tx]
  {:id       (.getId tx)
   :source   (.getSource tx)
   :amount   (.getAmount tx)
   :currency (.getCurrency tx)
   :net      (.getNet tx)
   :type     (.getType tx)
   ;; TODO: convert to UTC date with clj-time
   :created  (.getCreated tx)
   :available-on (.getAvailableOn tx)
   :status       (.getStatus tx)
   :fee          (.getFee tx)
   :fee-details  (fees->seq (.getFeeDetails tx))
   :description  (.getDescription tx)
   :__origin__   tx})

(defn ^List balance-txs->seq
  [^List xs]
  (doall (map balance-tx->map xs)))

(defn balance-tx-coll->seq
  [^StripeCollection txs]
  ;; TODO: pagination
  (map balance-tx->map (.getData txs)))

(defn ^IPersistentMap refund->map
  [^Refund r]
  {:id       (.getId r)
   :amount   (.getAmount r)
   ;; TODO: convert to UTC date with clj-time
   :created  (.getCreated r)
   :currency (.getCurrency r)
   :balance-transactions (.getBalanceTransaction r)
   :charge   (.getCharge r)
   :metadata (into {} (.getMetadata r))
   :__origin__ r})

(defn refunds-coll->seq
  [^StripeCollectionAPIResource xs]
  (map refund->map (.getData xs)))

(defn ^IPersistentMap dispute->map
  [^Dispute d]
  {:charge   (.getCharge d)
   :amount   (.getAmount d)
   :status   (.getStatus d)
   :currency (.getCurrency d)
   ;; TODO: convert to UTC date with clj-time
   :created  (.getCreated d)
   :live-mode? (.getLivemode d)
   :evidence   (.getEvidence d)
   ;; TODO: convert to UTC date with clj-time
   :evidence-due-by (.getEvidenceDueBy d)
   :reason          (.getReason d)
   :balance-transactions (balance-txs->seq (.getBalanceTransactions d))
   :metadata             (into {} (.getMetadata d))
   :__origin__           d})

(defn ^IPersistentMap charge->map
  [^Charge c]
  {:id         (.getId c)
   :currency   (.getCurrency c)
   :amount     (.getAmount c)
   :created    (.getCreated c)
   :live-mode? (.getLivemode c)
   :paid?      (.getPaid c)
   :refunded?       (.getRefunded c)
   :amount-refunded (.getAmountRefunded c)
   :refunds    (doall (map refund->map (if-let [^StripeCollectionAPIResource xs (.getRefunds c)]
                                         (.getData xs)
                                         [])))
   :captured?  (.getCaptured c)
   :dispute    (when-let [d (.getDispute c)]
                 (dispute->map d))
   :card       (card->map (.getCard c))
   :description           (.getDescription c)
   :statement-description (.getStatementDescription c)
   :invoice               (.getInvoice c)
   :customer              (.getCustomer c)
   :failure-message       (.getFailureMessage c)
   :failure-code          (.getFailureCode c)
   :__origin__            c})

(declare discount->map)
(declare plan->map)
(defn ^IPersistentMap subscription->map
  [^Subscription s]
  {:id                     (.getId s)
   ;; TODO: convert to UTC date with clj-time
   :current-period-start   (.getCurrentPeriodStart s)
   ;; TODO: convert to UTC date with clj-time
   :current-period-end     (.getCurrentPeriodEnd s)
   :cancel-at-period-end   (.getCancelAtPeriodEnd s)
   :customer               (.getCustomer s)
   :start                  (.getStart s)
   :status                 (.getStatus s)
   ;; TODO: convert to UTC date with clj-time
   :trial-start            (.getTrialStart s)
   ;; TODO: convert to UTC date with clj-time
   :trial-end              (.getTrialEnd s)
   ;; TODO: convert to UTC date with clj-time
   :cancelled-at           (.getCanceledAt s)
   ;; TODO: convert to UTC date with clj-time
   :ended-at               (.getEndedAt s)
   :quantity               (.getQuantity s)
   :discount               (when-let [d (.getDiscount s)]
                             (discount->map d))
   :application-fee-percent (.getApplicationFeePercent s)
   :plan                    (when-let [p (.getPlan s)]
                              (plan->map p))
   :metadata                (into {} (.getMetadata s))
   :__origin__             s})

(defn subscriptions-coll->seq
  [^StripeCollectionAPIResource xs]
  (map subscription->map (.getData xs)))

(defn ^IPersistentMap coupon->map
  [^Coupon c]
  {:id          (.getId c)
   :percent_off (.getPercentOff c)
   :amount_off  (.getAmountOff c)
   :currency    (.getCurrency c)
   :duration    (.getDuration c)
   :live-mode?  (.getLivemode c)
   :duration-in-months (.getDurationInMonths c)
   :max-redemptions    (.getMaxRedemptions c)
   :redeem-by          (.getRedeemBy c)
   :times-redeemed     (.getTimesRedeemed c)
   :valid?             (.getValid c)
   :metadata           (into {} (.getMetadata c))
   :__origin__ c})

(defn coupons-coll->seq
  [^StripeCollection xs]
  (map coupon->map (.getData xs)))

(defn ^IPersistentMap deleted-coupon->map
  [^DeletedCoupon c]
  {:id       (.getId c)
   :deleted? (.getDeleted c)})

(defn ^IPersistentMap discount->map
  [^Discount d]
  {:id         (.getId d)
   ;; TODO: convert to UTC date with clj-time
   :start      (.getStart d)
   ;; TODO: convert to UTC date with clj-time
   :end          (.getEnd d)
   :customer     (.getCustomer d)
   :subscription (.getSubscription d)
   :coupon       (when-let [c (.getCoupon d)]
                   (coupon->map c))
   :__origin__ d})

(defn ^IPersistentMap next-recurring-charge->map
  [^NextRecurringCharge nrc]
  {:amount     (.getAmount nrc)
   ;; TODO: convert to UTC date with clj-time
   :date       (.getData nrc)
   :__origin__ nrc})

(defn ^IPersistentMap customer->map
  [^Customer c]
  {:id             (.getId c)
   :description    (.getDescription c)
   :default-card   (.getDefaultCard c)
   :default-source (.getDefaultSource c)
   :email          (.getEmail c)
   :account-balance (.getAccountBalance c)
   :delinquent?     (.getDelinquent c)
   :next-recurring-charge (when-let [nrc (.getNextRecurringCharge c)]
                            (next-recurring-charge->map nrc))
   :cards           (doall (map card->map (if-let [^StripeCollectionAPIResource xs (.getCards c)]
                                            (.getData xs)
                                            [])))
   :subscriptions   (doall (map subscription->map (if-let [^StripeCollectionAPIResource xs (.getSubscriptions c)]
                                                    (.getData xs)
                                                    [])))
   ;; TODO: convert to UTC date with clj-time
   :created      (.getCreated c)
   :discount     (when-let [d (.getDiscount c)]
                   (discount->map d))
   ;; TODO: convert to UTC date with clj-time
   :trial-end    (.getTrialEnd c)
   :live-mode?   (.getLivemode c)
   :deleted?     (.getDeleted c)
   :metadata     (into {} (.getMetadata c))
   :__origin__  c})

(declare plan->map)
(defn plans-coll->seq
  [^StripeCollection xs]
  (map plan->map (.getData xs)))

(defn ^IPersistentMap plan->map
  [p]
  {:id         (.getId p)
   :amount     (.getAmount p)
   :currency   (.getCurrency p)
   :interval       (.getInterval p)
   :interval-count (.getIntervalCount p)
   :name           (.getName p)
   :trial-period-days     (.getTrialPeriodDays p)
   :statement-description (.getStatementDescription p)
   :live-mode?            (.getLivemode p)
   :metadata              (into {} (.getMetadata p))
   :__origin__ p})

(defn customers-coll->seq
  [^StripeCollection xs]
  (map customer->map (.getData xs)))

(defn ^IPersistentMap deleted-customer->map
  [^DeletedCustomer c]
  {:id       (.getId c)
   :deleted? (.getDeleted c)})
