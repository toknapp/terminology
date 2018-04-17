import $ivy.`co.upvest::terminology:0.1.0-SNAPSHOT`

// If you have some wrappers describing some terms in your model ...
case class Household(owner: String) extends AnyVal
case class Owner(name: String) extends AnyVal

// ... and some adjective or property that describes entities
case class Rich[T](t: T) extends AnyVal
object Rich {
  import terminology.adjectives.semiauto._
  implicit val adj = deriveAdjective[Rich]
}

import terminology.adjectives.implicits._

// Drop adjectives by using .as[T] syntax
Rich(Household("foo")).as[Household] : Household

// Map on underlying type using mapU syntax
Rich(Household("foo")) mapU { (h: Household) => Owner(h.owner) } : Rich[Owner]

// There are predefined adjectives
import terminology.adjectives.common._

case class Password(pw: String) extends AnyVal {
  def length = PasswordLength(pw.length)
}
case class PasswordLength(n: Int) extends AnyVal

val passwordStretching: Weak[Password] => Strong[Password] = {
  case Weak(pw) => /* do something clever */; Strong(pw)
}

Secret(Weak(Password("1234"))) mapU { p: Password => p.length } : Secret[Weak[PasswordLength]]

Secret(Weak(Password("1234"))) mapU passwordStretching : Secret[Strong[Password]]
