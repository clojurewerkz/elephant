(defproject clojurewerkz/elephant "1.0.0-beta18-SNAPSHOT"
  :url         "https://github.com/clojurewerkz/elephant"
  :license     {:name "Eclipse Public License"
                :url "http://www.eclipse.org/legal/epl-v10.html"}
  :description "Stripe API client in Clojure"
  :dependencies [[org.clojure/clojure    "1.8.0"]
                 [com.stripe/stripe-java "2.8.0"]]
  :profiles {:1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :1.9 {:dependencies [[org.clojure/clojure "1.9.0-alpha11"]]}
             :master {:dependencies [[org.clojure/clojure "1.9.0-master-SNAPSHOT"]]}
             :dev {:resource-paths ["test/resources"]
                   :plugins [[lein-codox "0.9.0"]]
                   :codox {:sources ["src/clojure"]}}}
  :aliases {"all" ["with-profile" "dev:dev,1.6:dev,1.7:dev,1.9:dev,master"]}
  :repositories {"sonatype" {:url "http://oss.sonatype.org/content/repositories/releases"
                             :snapshots false
                             :releases {:checksum :fail}}
                 "sonatype-snapshots" {:url "http://oss.sonatype.org/content/repositories/snapshots"
                                       :snapshots true
                                       :releases {:checksum :fail :update :always}}}
  :javac-options      ["-target" "1.6" "-source" "1.6"]
  :jvm-opts           ["-Dfile.encoding=utf-8"]
  :source-paths       ["src/clojure"]
  :java-source-paths  ["src/java"]
  :test-selectors {:default (constantly true)
                   :focus   :focus
                   :all     (constantly true)})
