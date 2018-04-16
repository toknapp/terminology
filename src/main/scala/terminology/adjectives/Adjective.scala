package terminology.adjectives

trait Adjective[F[_]] {
  def drop[U](f: F[U]): U
  def mk[R](r: R): F[R]
}

trait AdjectiveImplicits {
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
}
