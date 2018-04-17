package co.upvest.terminology.adjectives

import semiauto._

package object common {

  // Weak - Strong
  case class Weak[T](t: T) extends AnyVal
  object Weak { implicit val adj = deriveAdjective[Weak] }

  case class Strong[T](t: T) extends AnyVal
  object Strong { implicit val adj = deriveAdjective[Strong] }

  implicit def strongWeakRelation[T](s: Strong[T]): Weak[T] = Weak(s.t)


  // Public - Secret
  case class Public[T](t: T) extends AnyVal
  object Public { implicit val adj = deriveAdjective[Public] }

  case class Secret[T](t: T) extends AnyVal {
    override def toString() = "<obfuscated>"
  }
  object Secret { implicit val adj = deriveAdjective[Secret] }


  // Encrypted - PlainText
  case class PlainText[T](t: T) extends AnyVal
  object PlainText { implicit val adj = deriveAdjective[PlainText] }

  case class Encrypted[T](t: T) extends AnyVal
  object Encrypted { implicit val adj = deriveAdjective[Encrypted] }


  // Deterministic - Nondeterministic
  case class Deterministic[T](t: T) extends AnyVal
  object Deterministic { implicit val adj = deriveAdjective[Deterministic] }

  case class Nondeterministic[T](t: T) extends AnyVal
  object Nondeterministic { implicit val adj = deriveAdjective[Nondeterministic] }


  // Hashed
  case class Hashed[T](t: T) extends AnyVal
  object Hashed { implicit val adj = deriveAdjective[Hashed] }


  // Entropic
  //   (i.e. draw from a stochastical distribution using an entropy source)
  case class Entropic[T](t: T) extends AnyVal
  object Entropic { implicit val adj = deriveAdjective[Entropic] }
}
