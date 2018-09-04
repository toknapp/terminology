package co.upvest.terminology

import adjectives.common._

import org.scalatest.{WordSpec, Matchers}

class AdjectivesSpec extends WordSpec with Matchers {
  "Adjectives" should {
    "mapU" should {
      import adjectives.syntax.mapU

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
      import adjectives.syntax.as

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

    "peal" should {
      import adjectives.syntax.peal

      "unwrap one adjectives" in {
        typed[String] {
          Hashed("lol").peal
        }
      }

      "unwrap two adjectives" in {
        typed[String] {
          Weak(Secret("lol")).peal
        }
      }

      "unwrap three adjectives" in {
        typed[String] {
          Hashed(Weak(Secret("lol"))).peal
        }
      }
    }

    "cast" should {
      import adjectives.syntax.cast

      "unwrap one adjectives automatically" in {
        typed[String] { Hashed("lol") }
      }

      "unwrap two adjectives automatically" in {
        typed[String] { Weak(Secret("lol")) }
      }

      "unwrap three adjectives automatically" in {
        typed[String] { Hashed(Weak(Secret("lol"))) }
      }
    }
  }
}
