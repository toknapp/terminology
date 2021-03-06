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
```scala
// If you have some wrappers describing some terms in your model ...
case class Household(owner: String) extends AnyVal
case class Owner(name: String) extends AnyVal

// ... and some adjective or property that describes entities.
case class Rich[T](t: T) extends AnyVal

object Rich {
  import co.upvest.terminology.adjectives.semiauto._
  implicit val adj = deriveAdjective[Rich]
}

// If we import some implicits giving us access to the syntaxes...
import co.upvest.terminology.adjectives.implicits._

// ... we can drop adjectives by using `.as[T]` syntax,
Rich(Household("foo")).as[Household] : Household

// and map on underlying type using `mapU` syntax.
Rich(Household("foo")) mapU { (h: Household) => Owner(h.owner) } : Rich[Owner]


// There are some predefined adjectives.
import co.upvest.terminology.adjectives.common._

// If we have a model password and its length:
case class Password(pw: String) extends AnyVal {
  def length = PasswordLength(pw.length)
}
case class PasswordLength(n: Int) extends AnyVal

// Then we can map the inner-most type (`Password`) as we did above
Secret(Weak(Password("1234"))) mapU { p: Password => p.length } : Secret[Weak[PasswordLength]]

// but we can also let `mapU` select the nesting-level specified by the
// mapped function type:

val passwordStretching: Weak[Password] => Strong[Password] = {
  case Weak(pw) => /* do something clever */; Strong(pw)
}

Secret(Weak(Password("1234"))) mapU passwordStretching : Secret[Strong[Password]]

// We can also make descriptions of our models:
import co.upvest.terminology.description.Description
object Password {
  implicit val description: Description[Password, Weak[Entropic[Array[Byte]]]] = {
    case Password(s) => Weak(Entropic(s.getBytes))
  }
}

// which here expresses that in general users can't be trusted to pick a strong
// password.

import co.upvest.terminology.description.implicits._

Password("1234").description : Weak[Entropic[Array[Byte]]]


// If we then have a cipher algorithm that we want to use:
def cipher(pt: PlainText[Array[Byte]], key: Strong[Entropic[Array[Byte]]]): Encrypted[Array[Byte]] =
  Encrypted(/* some clever transformation using `key` */ pt.as[Array[Byte]])

// We get a compilation error if we try to use the password:
val msg = PlainText("lol".getBytes)
val pwd = Password("1234")
shapeless.test.illTyped("""cipher(msg, pwd)""")

// But if we apply some key stretching:
val keyStretching: Weak[Entropic[Array[Byte]]] => Strong[Entropic[Array[Byte]]] = {
  case Weak(ebs) => /* do something clever */ Strong(ebs)
}

// then the type-checker says we're good to go:
cipher(msg, keyStretching(pwd.description))

// The described types also get access to the `as` and `mapU` syntaxes:
pwd.as[Array[Byte]] : Array[Byte]
pwd mapU { bs: Array[Byte] => new String(bs) } : Weak[Entropic[String]]
```
