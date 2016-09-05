(ns clojurewerkz.elephant.util
  (:import [com.stripe.net RequestOptions]))

(defn api-key->request-options
  [api-key]
  (-> (RequestOptions/builder)
      (.setApiKey api-key)
      .build))
