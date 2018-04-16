package terminology.adjectives

// Dichotomies
//   (eg it's a contradiction for a thing to be both weak and strong)
//   see the compilable example below

class Dichotomy[F[_], G[_], A](f: A => F[A], g: A => G[A])

object laws {
  implicit def dichotomyContradiction[F[_], G[_], A](a: A)(implicit
    D: Dichotomy[F, G, A],
  ): Nothing = ???
}
