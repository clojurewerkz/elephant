## Changes Between 1.0.0-beta6 and 1.0.0-beta7

### Stripe Java Client Upgrade

Stripe Java client was upgraded to `1.23.0`.



## Changes Between 1.0.0-beta5 and 1.0.0-beta6

### customers/list

`clojurewerkz.elephant.customers/list` is a new function
that lists customers:

``` clojure
(require '[clojurewerkz.elephant.customers :as ecr])

(ecr/list)
(ecr/list {"limit" 100})
```

## Changes Between 1.0.0-beta4 and 1.0.0-beta5

### customers/retrieve-or-create

`clojurewerkz.elephant.customers/retrieve-or-create` is a new function
that idempotently creates a customer:

``` clojure
(require '[clojurewerkz.elephant.customers         :as ecr])

;; will create the customer on first invocation,
;; fetch and return it on subsequence ones
(ecr/retrieve-or-create "customer-id" customer)
```

### Clojure 1.7 Compatibility

The library now compiles with Clojure 1.7.


## Changes Between 1.0.0-beta3 and 1.0.0-beta4

### More Sensible Charge and Transfer Enabled Values 

`charge-` and `transfer-enabled` values in customer maps now return `false`
instead of `nil`.


## Changes Between 1.0.0-beta2 and 1.0.0-beta3

### Upgrade to Stripe Java client 1.19.1

Stripe Java client was upgraded to `1.19.1`.


## Changes Between 1.0.0-beta1 and 1.0.0-beta2

### plans/retrieve-or-create

`clojurewerkz.elephant.plans/retrieve-or-create` is a new function
that idempotently creates a plan:

``` clojure
(require '[clojurewerkz.elephant.plans         :as ep])

;; will create the plan on first invocation,
;; fetch and return it on subsequence ones
(ep/retrieve-or-create "plan-id" {"amount"         100
                                  "currency"       "usd"
                                  "interval"       "month"
                                  "interval_count" 2
                                  "name"           "J Bindings Plan"})
```


## Changes Between 1.0.0-alpha1 and 1.0.0-beta1

### Subscriptions

`clojurewerkz.elephant.plans/create` is a new function
that creates a subscription for provided customer:

``` clojure
(require '[clojurewerkz.elephant.customers     :as ecr])
(require '[clojurewerkz.elephant.plans         :as ep])
(require '[clojurewerkz.elephant.subscriptions :as esub])

(let [p (ep/create (unique-plan plan))
      c (ecr/create customer)]
  (esub/create c {"plan" (:id p)}))
```

`clojurewerkz.elephant.plans/list` lists customer subscriptions:

``` clojure
(require '[clojurewerkz.elephant.customers     :as ecr])
(require '[clojurewerkz.elephant.plans         :as ep])
(require '[clojurewerkz.elephant.subscriptions :as esub])

(let [p  (ep/create (unique-plan plan))
      c  (ecr/create customer)
      x  (esub/create c {"plan" (:id p)})]
  (esub/list c))
```

`clojurewerkz.elephant.plans/update` updates a subscription:

``` clojure
(require '[clojurewerkz.elephant.customers     :as ecr])
(require '[clojurewerkz.elephant.plans         :as ep])
(require '[clojurewerkz.elephant.subscriptions :as esub])

(let [p1 (ep/create (unique-plan plan))
      p2 (ep/create (unique-plan plan))
      c  (ecr/create customer)
      x  (esub/create c {"plan" (:id p1)})]
  (esub/update x {"plan" (:id p2)}))
```

`clojurewerkz.elephant.plans/cancel` cancels a subscription:

``` clojure
(require '[clojurewerkz.elephant.customers     :as ecr])
(require '[clojurewerkz.elephant.plans         :as ep])
(require '[clojurewerkz.elephant.subscriptions :as esub])

(let [p (ep/create (unique-plan plan))
      c (ecr/create customer)
      x (esub/create c {"plan" (:id p)})]
  (esub/cancel x))
```


### Plans

`clojurewerkz.elephant.plans/create` is a new function
that creates a (subscription) plan:

``` clojure
(p/create {"id"             (str (java.util.UUID/randomUUID))
           "amount"         10
           "currency"       "usd"
           "interval"       "month"
           "interval_count" 2
           "name"           "Basic Plan"})
```

### Customers

`clojurewerkz.elephant.customers/create` is a new function
that creates a customer:

``` clojure
(c/create {"card"        cc
           "description" "A. N. Awesome Customer"})
```

`clojurewerkz.elephant.customers/retrieve` fetches customer
information by id:

``` clojure
(c/retrieve "cus_4hnqECORF7glfI")
```

### Refunds Options, Partial Refunds

`clojurewerkz.elephant.charges/refund` now supports
refund options, e.g. to perform partial refunds:

``` clojure
(let [x (ech/create opts)]
  (ech/refund x {"amount" 50}))
```

### charges/capture

`clojurewerkz.elephant.charges/capture` is a new function that captures
a previously uncaptured charge:

``` clojure
(let [x (ech/create opts)]
  (ech/capture x))
```

## 1.0.0-alpha1

Initial release. Roughly 25% of the tests are ported.
