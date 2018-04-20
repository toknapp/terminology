#!/bin/sh

TARGET=README.md

cat > $TARGET <<EOF
# Terminology

[![CircleCI](https://circleci.com/gh/toknapp/terminology/tree/master.svg?style=svg)](https://circleci.com/gh/toknapp/terminology/tree/master)

Yet another tagging library for Scala to effectively reason about your
terminology.

This one aims to syntax support for mapping and casting (dropping of tags) to
your own properties, adjectives and descriptor types via type-classes with the
aid of [semiauto](./src/main/scala/terminology/adjectives/semiauto.scala) derivation via
[Generic1](https://github.com/milessabin/shapeless/blob/master/core/src/main/scala/shapeless/generic1.scala)).

Similar projects:
* [supertagged](https://github.com/rudogma/scala-supertagged)
* Scalaz' [Tag](https://github.com/scalaz/scalaz/blob/series/7.3.x/core/src/main/scala/scalaz/Tag.scala)
* Shapeless' [Tagged](https://github.com/milessabin/shapeless/blob/master/core/src/main/scala/shapeless/typeoperators.scala#L25)

## Example
The following example is an [runnable file](./src/test/resources/example.sc) using
[Ammonite](https://github.com/lihaoyi/Ammonite) and is
[verified to compile and run](https://github.com/toknapp/terminology/blob/6b357b04881fef72f210ea1c65516abd14be0e82/build.sbt#L23)
during the [CI-cycle](https://github.com/toknapp/terminology/blob/6b357b04881fef72f210ea1c65516abd14be0e82/.circleci/config.yml#L52).
\`\`\`scala
$(cat src/test/resources/example.sc)
\`\`\`
EOF
