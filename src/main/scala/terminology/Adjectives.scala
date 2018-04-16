package terminology

import shapeless._

package object adjectives {

  trait Adjective[F[_]] {
    def drop[U](f: F[U]): U
    def mk[R](r: R): F[R]
  }

  trait AdjectiveMapper[F[_], U, R] {
    type Out[S]
    def mapU[S](fu: F[R], f: U => S): F[Out[S]]
  }

  implicit def adjMap[F[_], U](implicit A: Adjective[F]) =
    new AdjectiveMapper[F, U, U] {
      type Out[S] = S
      def mapU[S](fu: F[U], f: U => S): F[Out[S]] = A.mk(f(A.drop(fu)))
    }

  implicit def adjMap2[F[_], G[_], U, R](implicit A: Adjective[F], AM: AdjectiveMapper[G, U, R]) =
    new AdjectiveMapper[F, U, G[R]] {
      type Out[S] = G[AM.Out[S]]
      def mapU[S](fu: F[G[R]], f: U => S): F[Out[S]] = A.mk(AM.mapU(A.drop(fu), f))
    }

  implicit class AdjOps[F[_], T](ft: F[T]) {
    def mapU[U, R](f: U => R)(implicit AM: AdjectiveMapper[F, U, T]): F[AM.Out[R]] =
      AM.mapU(ft, f)
  }

  //implicit def dropAdj[F[_], U](f: F[U])(implicit Adj: Adjective[F, U]): U =
    //Adj.drop(f)

  trait Point[R[_]] {
    def point[A](a: A): R[A]
    def copoint[A](ra: R[A]): A
  }

  object Point {
    implicit def hlist = new Point[? :: HNil] {
      def point[A](a: A): A :: HNil = a :: HNil
      def copoint[A](hl: A :: HNil): A = hl.head
    }
  }

  def deriveAdjective[F[_]](implicit gen: Generic1[F, Point]): Adjective[F] =
    new Adjective[F] {
      def drop[U](x: F[U]): U = gen.fr.copoint(gen.to(x))
      def mk[R](r: R): F[R] = gen.from(gen.fr.point(r))
    }

  object implicits extends AdjectiveImplicits0

  trait AdjectiveImplicits0 extends AdjectiveImplicits1 {
    //implicit class AdjectiveOps3[G[_], F[_], H[_], U](fghu: F[G[H[U]]])(implicit Adj: Adjective[λ[α => F[G[H[α]]]]]) {
      //def drop: U = Adj.drop(fghu)
      //def mapU[R](f: U => R): F[G[H[R]]] = Adj.mapU(fghu, f)
    //}

    //implicit def stackedAdj[F[_], G[_]](implicit
      //af: Adjective[F],
      //ag: Adjective[G]
    //): Adjective[λ[α => F[G[α]]]] = new Adjective[λ[α => F[G[α]]]] {
      //def drop[U](x: F[G[U]]): U = ag.drop(af.drop(x))
      //def mk[R](r: R): F[G[R]] = af.mk(ag.mk(r))
    //}
  }

  trait AdjectiveImplicits1 {
    //implicit class AdjectiveOps2[G[_], F[_], U](fgu: F[G[U]])(implicit Adj: Adjective[λ[α => F[G[α]]]]) {
      //def drop: U = Adj.drop(fgu)
      //def mapU[R](f: U => R): F[G[R]] = Adj.mapU(fgu, f)
    //}
    //implicit class AdjectiveOps[F[_], U](fu: F[U])(implicit Adj: Adjective[F]) {
      //def drop: U = Adj.drop(fu)
      //def mapU[R](f: U => R): F[R] = Adj.mapU(fu, f)
    //}

  }

  case class Weak[T](t: T)
  object Weak {
    implicit val weak = deriveAdjective[Weak]
  }

  case class Strong[T](t: T)
  implicit def strongWeakRelation[T](s: Strong[T]): Weak[T] = Weak(s.t)

  case class Hashed[T](t: T)
  object Hashed {
    implicit val hashed = deriveAdjective[Hashed]
  }


  // Dichotomies
  //   (eg it's a contradiction for a thing to be both weak and strong)
  //   see the compilable example below

  class Dichotomy[F[_], G[_], A](f: A => F[A], g: A => G[A])

  implicit def dichotomyContradiction[F[_], G[_], A](a: A)(implicit
    D: Dichotomy[F, G, A],
  ): Nothing = ???

  case class PlainText[T](t: T)
  case class Encrypted[T](t: T)
  implicit def plainTextEncryptedDichotomy[A](implicit
    f: A => PlainText[A],
    g: A => Encrypted[A]
  ) = new Dichotomy[PlainText, Encrypted, A](f, g)

  case class Public[T](t: T)
  case class Secret[T](unwrapSecret: T) {
    override def toString() = "<obfuscated>"
    protected[terminology] val t = unwrapSecret
  }

  object Secret {
    implicit val secret = deriveAdjective[Secret]
  }

  Hashed(Weak(Secret("lol"))) mapU { (s: String) => s.length } : Hashed[Weak[Secret[Int]]]

  Hashed(Weak(Secret("lol"))) mapU { (s: Secret[String]) => 7 } : Hashed[Weak[Int]]
}
