(ns clojurewerkz.elephant.test-helpers
  (:require [clojurewerkz.elephant.client :as ec]
            [clojure.java.io :as io]))

(def ^:private slurp-key
  (memoize (fn [^String res]
             (-> (io/resource res)
                 slurp
                 .trim))))

(defn set-up-stripe-test-key
  [f]
  (let [key (slurp-key "api_key.txt")]
    (ec/set-api-key! key))
  (f))
