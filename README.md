# Elephant, Stripe Clojure Client

Elephant is a modern Clojure client for the [Stripe API](https://stripe.com/docs/api).

## Project Goals

 * Provide an API that allows developers primarily work with immutable
Clojure data structures.
 * Have good test coverage.


## Project Maturity

Elephant is *very* young.


## Artifacts

Elephant artifacts are [released to Clojars](https://clojars.org/clojurewerkz/elephant).
If you are using Maven, add the following repository
definition to your `pom.xml`:

``` xml
<repository>
  <id>clojars.org</id>
  <url>http://clojars.org/repo</url>
</repository>
```

### The Most Recent Release

With Leiningen:

    [clojurewerkz/elephant "1.0.0-beta1"]


With Maven:

    <dependency>
      <groupId>clojurewerkz</groupId>
      <artifactId>elephant</artifactId>
      <version>1.0.0-beta1</version>
    </dependency>


## Documentation & Examples

The project is too young to begin documenting the API.

This section will be updated when the API stabilizes.


## Community & Support

To subscribe for announcements of releases, important changes and so on,
please follow [@ClojureWerkz](https://twitter.com/clojurewerkz) on Twitter.



## Supported Clojure versions

Elephant requires Clojure 1.6+.


## Continuous Integration Status

[![Continuous Integration status](https://secure.travis-ci.org/clojurewerkz/elephant.png)](http://travis-ci.org/clojurewerkz/elephant)


## Elephant Is a ClojureWerkz Project

elephant is part of the [group of Clojure libraries known as ClojureWerkz](http://clojurewerkz.org), together with

 * [Monger](http://clojuremongodb.info)
 * [Langohr](http://clojurerabbitmq.info)
 * [Elastisch](http://clojureelasticsearch.info)
 * [Cassaforte](http://clojurecassandra.info)
 * [Titanium](http://titanium.clojurewerkz.org)
 * [Neocons](http://clojureneo4j.info)
 * [Meltdown](https://github.com/clojurewerkz/meltdown)

and several others.


## Development

Elephant uses [Leiningen 2](https://github.com/technomancy/leiningen/blob/master/doc/TUTORIAL.md). Make
sure you have it installed and then run tests against supported
Clojure versions using

    lein2 all test

Then create a branch and make your changes on it. Once you are done
with your changes and all tests pass, submit a pull request on GitHub.


## License

Copyright (C) 2014 Michael S. Klishin, Alex Petrov, and The ClojureWerkz Team.

Double licensed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html) (the same as Clojure) or
the [Apache Public License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).


## FAQ

### What's in the Name?

The library is inspired by a badass USSR cartoon about two detectives
investigating theft of a striped elephant.
