package co.upvest.terminology

import adjectives.common._

import description.Description

import org.scalatest.{WordSpec, Matchers}

class DescriptionSpec extends WordSpec with Matchers {
  case class Foo(i: Int)
  object Foo {
    implicit val description: Description[Foo, Strong[Secret[Int]]] = {
      case Foo(i) => Strong(Secret(i))
    }
  }

  "Description" should {
    "describe types" in {
      import description.syntax.description
      typed[Strong[Secret[Int]]] { Foo(8).description }
    }

    "support the adjectives' as syntax" in {
      import description.syntax.as
      typed[Secret[Int]] { Foo(8).as[Secret[Int]] }
    }

    "support the adjectives' mapU syntax" in {
      import description.syntax.mapU
      typed[Strong[Secret[String]]] { Foo(8) mapU { i: Int => i.toString } }
    }
  }
}
