package co.upvest.terminology.adjectives

import semiauto._

package object common {

  case class Hashed[T](t: T)
  object Hashed { implicit val adj = deriveAdjective[Hashed] }

  // Weak - Strong

  case class Weak[T](t: T)
  object Weak { implicit val adj = deriveAdjective[Weak] }

  case class Strong[T](t: T)
  object Strong { implicit val adj = deriveAdjective[Strong] }

  implicit def strongWeakRelation[T](s: Strong[T]): Weak[T] = Weak(s.t)


  // Public - Secret

  case class Public[T](t: T)
  object Public { implicit val adj = deriveAdjective[Public] }

  case class Secret[T](t: T) {
    override def toString() = "<obfuscated>"
  }
  object Secret { implicit val adj = deriveAdjective[Secret] }
}
