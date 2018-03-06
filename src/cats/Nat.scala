package klarna.fp

import scala.util.Try

import cats.~>
import cats.data.NonEmptyList

object Nat {

  // Natural transformations are a mapping between one type constructor of a
  // single argument to another.
  //
  // E.g:
  //
  // Try[_] => Option[_]
  //
  // In cats this is done using the type `~>[F[_], G[_]]`
  //
  // In scala we can use types defined with operators as infix:
  //
  // F ~> G =:= ~>[F, G]
  //
  // Note:
  //   `~>` is actually a type alias for `FunctionK`. The `K` at the end means
  //   that the type operates on higher-kinded types.
  //
  //   A kind describes the "arity" of a type. A simple type such as String has kind `*`
  //   whereas a type constructor such as Option as kind `* -> *`. Either takes 2 type parameters
  //   and therefore has kind `* -> * -> *`
  //
  //   This kind system allows the type checker to enforce kindness:
  //   Given:
  //
  //      def hkt[F[_]] = _ // F must have kind `* -> *`
  //
  //   The following should not compile:
  //
  //      hkt[Either]    // Either has incompatible kind `* -> * -> *`
  //      hkt[List[Int]] // List[Int] has incompatible kind `*`
  //
  //   Therefore, a higher-kinded type is simply a type whose kind is greater than `*`.


  /** Implements the natural transformation between `Try` and `Option` */
  object TryToOption extends (Try ~> Option) {
    def apply[A](a: Try[A]): Option[A] = ???
  }

  sealed trait Validation[+A, +B]
  case class Valid[B](value: B) extends Validation[Nothing, B]
  case class Recovered[A, B](value: B, errors: NonEmptyList[A]) extends Validation[A, B]
  case class Unrecoverable[A](errors: NonEmptyList[A]) extends Validation[A, Nothing]

  /** Implements a translation between `Validation` and `Either`
    *
    * This translation discards recovered errors and only turns the
    * Unrecoverable case into a `Left[NonEmptyList[L], ?]`
    */
  def ValidationToEither[L] = new (Validation[L, ?] ~> Either[NonEmptyList[L], ?]) {
    def apply[A](a: Validation[L, A]): Either[NonEmptyList[L], A] = ???
  }
}
