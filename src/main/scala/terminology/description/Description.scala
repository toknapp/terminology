package co.upvest.terminology.description


import co.upvest.terminology.adjectives.{AdjectiveDropper, AdjectiveMapper}

trait Description[A, D] extends (A => D)

trait DescriptionImplicits {
  implicit class DescriptionSyntax[D, A](a: A)(implicit D: Description[A, D]) {
    def description: D = D(a)
  }

  implicit class DescriptionAdjectiveSyntax[F[_], A, T](a: A)(implicit
    D: Description[A, F[T]],
    view: A => F[T]
  ) {
    def as[U](implicit AD: AdjectiveDropper[F, U, T]): U = AD.as(view(a))

    def mapU[U, R](f: U => R)(implicit
      AM: AdjectiveMapper[F, U, T]
    ): F[AM.Out[R]] = AM.mapU(view(a), f)
  }
}