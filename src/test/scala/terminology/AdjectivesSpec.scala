package co.upvest.terminology

import adjectives.common._
import adjectives.implicits._

import org.scalatest.{WordSpec, Matchers}

class AdjectivesSpec extends WordSpec with Matchers {
  "Adjectives" should {
    "mapU" in {
      typed[Hashed[Weak[Secret[Int]]]] {
        Hashed(Weak(Secret("lol"))) mapU { (s: String) => s.length }
      }

      typed[Hashed[Weak[Double]]] {
        Hashed(Weak(Secret("lol"))) mapU { (s: Secret[String]) => 7.0 }
      }

      typed[Hashed[Unit]] {
        Hashed(Weak(Secret("lol"))) mapU { (s: Weak[Secret[String]]) => () }
      }
    }

    "as" in {
      typed[Weak[Secret[String]]] {
        Hashed(Weak(Secret("lol"))).as[Weak[Secret[String]]]
      }

      typed[Secret[String]] {
        Hashed(Weak(Secret("lol"))).as[Secret[String]]
      }

      typed[String] {
        Hashed(Weak(Secret("lol"))).as[String]
      }
    }
  }
}
