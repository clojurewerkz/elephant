(ns clojurewerkz.elephant.client
  (:import com.stripe.Stripe))

;;
;; API
;;

(defn set-api-key!
  "Configures default Stripe API key to be used."
  [^String key]
  (set! (. Stripe apiKey) key))
