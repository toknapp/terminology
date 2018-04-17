package co.upvest.terminology.adjectives

trait Adjective[F[_]] {
  def label[R](r: R): F[R]
  def unlabel[U](f: F[U]): U
}

trait AdjectiveImplicits
  extends AdjectiveMapperImplicits
  with AdjectiveDropperImplicits

// To add syntax at the desired level of stacks of adjectives: F[G[H[_]]],
// I consider them as nils and cons cells and add implicits to look for the
// desried type, denoted `U` below.

trait AdjectiveMapperImplicits {
  trait AdjectiveMapper[F[_], U, R] {
    type Out[S]
    def mapU[S](fr: F[R], f: U => S): F[Out[S]]
  }

  implicit def adjMapNil[F[_], U](implicit A: Adjective[F]) =
    new AdjectiveMapper[F, U, U] {
      type Out[S] = S
      def mapU[S](fu: F[U], f: U => S): F[Out[S]] =
        A.label(f(A.unlabel(fu)))
    }

  implicit def adjMapCons[F[_], G[_], U, R](implicit
    A: Adjective[F],
    AM: AdjectiveMapper[G, U, R]
  ) = new AdjectiveMapper[F, U, G[R]] {
    type Out[S] = G[AM.Out[S]]
    def mapU[S](fgr: F[G[R]], f: U => S): F[Out[S]] =
      A.label(AM.mapU(A.unlabel(fgr), f))
  }

  implicit class MapUSyntax[F[_], T](ft: F[T]) {
    def mapU[U, R](f: U => R)(implicit
      AM: AdjectiveMapper[F, U, T]
    ): F[AM.Out[R]] = AM.mapU(ft, f)
  }
}

trait AdjectiveDropperImplicits {
  trait AdjectiveDropper[F[_], U, R] {
    def as(fr: F[R]): U
  }

  implicit def adjDropNil[F[_], U](implicit A: Adjective[F]) =
    new AdjectiveDropper[F, U, U] {
      def as(fu: F[U]): U = A.unlabel(fu)
    }

  implicit def adjDropCons[F[_], G[_], U, R](implicit
    A: Adjective[F],
    AD: AdjectiveDropper[G, U, R]
  ) = new AdjectiveDropper[F, U, G[R]] {
    def as(fgr: F[G[R]]): U = AD.as(A.unlabel(fgr))
  }

  implicit class AsSyntax[F[_], T](ft: F[T]) {
    def as[U](implicit AD: AdjectiveDropper[F, U, T]): U = AD.as(ft)
  }
}
