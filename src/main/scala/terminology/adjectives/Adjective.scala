package co.upvest.terminology.adjectives

trait Adjective[F[_]] {
  def label[R](r: R): F[R]
  def unlabel[U](f: F[U]): U
}

// To add syntax at the desired level of stacks of adjectives: F[G[H[_]]],
// I consider them as nils and cons cells and add implicits to look for the
// desried type, denoted `U` below.

trait AdjectiveMapper[F[_], U, R] {
  type Out[S]
  def mapU[S](fr: F[R], f: U => S): F[Out[S]]
}

trait AdjectiveMapperImplicits {
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
}

object AdjectiveMapper extends AdjectiveMapperImplicits


trait AdjectiveDropper[F[_], U, R] {
  def as(fr: F[R]): U
}

trait AdjectiveDropperImplicitsLower {
  implicit def adjDropNil[F[_], U](implicit A: Adjective[F]) =
    new AdjectiveDropper[F, U, U] {
      def as(fu: F[U]): U = A.unlabel(fu)
    }
}

trait AdjectiveDropperImplicits extends AdjectiveDropperImplicitsLower {

  implicit def adjDropCons[F[_], G[_], U, R](implicit
    A: Adjective[F],
    AD: AdjectiveDropper[G, U, R]
  ) = new AdjectiveDropper[F, U, G[R]] {
    def as(fgr: F[G[R]]): U = AD.as(A.unlabel(fgr))
  }
}

object AdjectiveDropper extends AdjectiveDropperImplicits

trait AdjectiveOps {
  implicit class as[F[_], T](ft: F[T]) {
    def as[U](implicit AD: AdjectiveDropper[F, U, T]): U = AD.as(ft)
  }

  implicit class peal[F[_], T, U](ft: F[T])(implicit AD: AdjectiveDropper[F, U, T]) {
    def peal: U = AD.as(ft)
  }

  implicit class mapU[F[_], T](ft: F[T]) {
    def mapU[U, R](f: U => R)(implicit
      AM: AdjectiveMapper[F, U, T]
    ): F[AM.Out[R]] = AM.mapU(ft, f)
  }

  implicit def cast[F[_], T, U](ft: F[T])(implicit AD: AdjectiveDropper[F, U, T]): U = AD.as(ft)
}
