(ns clojurewerkz.elephant.plans
  (:require [clojurewerkz.elephant.conversion :as cnv]
            [clojure.walk :as wlk])
  (:import [com.stripe.model Plan]
           [clojure.lang IPersistentMap]))

;;
;; API
;;

(defn create
  [^IPersistentMap opts]
  (cnv/plan->map (Plan/create (wlk/stringify-keys opts))))
