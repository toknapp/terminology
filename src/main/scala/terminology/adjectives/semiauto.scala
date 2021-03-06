package co.upvest.terminology.adjectives

import shapeless._

package object semiauto {

  def deriveAdjective[F[_]](implicit
    gen: Generic1[F, internal.Point]
  ): Adjective[F] = new Adjective[F] {
    def label[R](r: R): F[R] = gen.from(gen.fr.point(r))
    def unlabel[U](x: F[U]): U = gen.fr.copoint(gen.to(x))
  }

  object internal {
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
  }
}
