## Changes Between 1.0.0-alpha1 and 1.0.0-alpha2

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
