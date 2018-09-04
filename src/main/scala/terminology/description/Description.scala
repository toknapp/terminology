package co.upvest.terminology.description


import co.upvest.terminology.adjectives.{AdjectiveDropper, AdjectiveMapper}

trait Description[A, D] extends (A => D)

trait DescriptionOpsLower {
  implicit class DescriptionSyntax[D, A](a: A)(implicit D: Description[A, D]) {
    def description: D = D(a)
  }

  implicit class DescriptionAsSyntax[F[_], A, T](a: A)(implicit
    D: Description[A, F[T]],
    view: A => F[T]
  ) {
    def as[U](implicit AD: AdjectiveDropper[F, U, T]): U = AD.as(view(a))
  }

  implicit class DescriptionMapUSyntax[F[_], A, T](a: A)(implicit
    D: Description[A, F[T]],
    view: A => F[T]
  ) {
    def mapU[U, R](f: U => R)(implicit
      AM: AdjectiveMapper[F, U, T]
    ): F[AM.Out[R]] = AM.mapU(view(a), f)
  }
}

trait DescriptionOps extends DescriptionOpsLower {
  implicit def description[D, A](a: A)(implicit
    D: Description[A, D]
  ) = new DescriptionSyntax(a)(D)

  implicit def as[F[_], A, T](a: A)(implicit
    D: Description[A, F[T]],
    view: A => F[T]
  ) = new DescriptionAsSyntax(a)(D, view)

  implicit def mapU[F[_], A, T](a: A)(implicit
    D: Description[A, F[T]],
    view: A => F[T]
  ) = new DescriptionMapUSyntax(a)(D, view)
}
