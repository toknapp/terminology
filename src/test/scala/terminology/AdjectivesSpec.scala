package co.upvest.terminology

import adjectives.common._
import adjectives.implicits._

import org.scalatest.{WordSpec, Matchers}

class AdjectivesSpec extends WordSpec with Matchers {
  "Adjectives" should {
    "mapU" should {
      "map the inner-most type" in {
        typed[Hashed[Weak[Secret[Int]]]] {
          Hashed(Weak(Secret("lol"))) mapU { (s: String) => s.length }
        }
      }

      "map a type in the middle" in {
        typed[Hashed[Weak[Double]]] {
          Hashed(Weak(Secret("lol"))) mapU { (s: Secret[String]) => 7.0 }
        }
      }

      "map on the outermost adjective" in {
        typed[Hashed[Unit]] {
          Hashed(Weak(Secret("lol"))) mapU { (s: Weak[Secret[String]]) => () }
        }
      }
    }

    "as" should {
      "discard the outermost adjective" in {
        typed[Weak[Secret[String]]] {
          Hashed(Weak(Secret("lol"))).as[Weak[Secret[String]]]
        }
      }

      "discard more than one adjective" in {
        typed[Secret[String]] {
          Hashed(Weak(Secret("lol"))).as[Secret[String]]
        }
      }

      "discard all adjectives" in {
        typed[String] {
          Hashed(Weak(Secret("lol"))).as[String]
        }
      }
    }
  }
}
