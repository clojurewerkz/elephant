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
