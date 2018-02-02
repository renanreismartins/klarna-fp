package klarna.fp
package data

import cats.effect.IO
import cats.{Applicative, Monad}
import cats.syntax.applicative._
import cats.syntax.functor._
import cats.data.{Kleisli, OptionT}

object KleisliTriples {

  /** At this point in our FP careers we know how to compose functions, but
   *  during our day-to-day work, we're mostly working in some context (`IO`,
   *  `Option`, `Either`).
   *
   * We need some way to be able to compose our API functions on the form:
   *
   * {{{
   * A => F[B]
   * B => F[C]
   * }}}
   *
   * it is very elegant to be able to express a transformation as:
   *
   * {{{
   * def foo[A, B, C]: A => C =
   *   f1 andThen f2
   * }}}
   *
   * vs
   *
   * {{{
   * def foo[A, B, C]: A => C =
   *   x => f2(f1(x))
   * }}}
   *
   * for a simple case both might look roughly equivalent - but the latter
   * quickly gets unwieldy as the number of functions grow. So we prefer the
   * first style for readability (but not performance).
   *
   * What happens then when we have some context?
   *
   * {{{
   * val f: A => Option[B] = a => Option(a.toB)
   * val g: B => Option[C] = b => Option(b.toc)
   *
   * f andThen g // doesn't compile 😞
   * }}}
   *
   * what now? We still want to be able to compose larger pipelines from small
   * functions returning some context.
   *
   * Kleisli to the rescue!
   * ======================
   * Kleisli is a type of arrow on a monadic context (say what?!). It is simply
   * a wrapper for the function:
   *
   * {{{
   * A => F[B]
   * }}}
   *
   * The wrapper simply allows you to compose the function that is contained
   * within.
   *
   * The definition looks like this:
   *
   * {{{
   * final case class Kleisli[F[_], A, B](run: A => F[B]) { ... }
   * }}}
   *
   * If we change our `f` and `g` to Kleislis, we can now compose them:
   *
   * {{{
   * val f: Kleisli[Option, A, B] = Kleisli(a => Option(a.toB))
   * val g: Kleisli[Option, B, C] = Kleisli(b => Option(b.toC))
   *
   * f andThen g // compiles! 😊
   * }}}
   *
   * Kleisli itself is also a monad, meaning that we can use the common
   * operations like `flatMap`, `map`, `pure` etc to work with this new
   * structure.
   */

  case class Request(method: String, path: String, body: String = "")
  case class Response(status: Int, body: String)

  type Service[F[_]] = Kleisli[F, Request, Option[Response]]

  object Service {
    /** This helper function allows you to create a `Service` using the following syntax:
     *  {{{
     *  Service[F] {
     *    case Request(_, _, _) => Response(...).pure[F]
     *  }
     *  }}}
     */
    def apply[F[_]: Applicative](pf: PartialFunction[Request, F[Response]]): Service[F] = Kleisli { req =>
      pf.andThen(_.map(Option.apply))
        .applyOrElse(req, Function.const((None: Option[Response]).pure[F]))
    }
  }

  // 1) Implement a service that responds to `GET /ping` with the response:
  //    "pong"
  def PingEndpoint: Service[IO] = ???

  // We can now express different parts of our pipeline using Kleisli. We can
  // have a `Source` of the pipeline be something that creates an `A` out of a
  // request in the context of `F`

  type Source[F[_], A]       = Kleisli[F, Request, A]

  // We can define a transformation as a Kleisli alias
  type Transform[F[_], A, B] = Kleisli[F, A, B]

  // We can define a `Sink` as taking an `A` and maybe creating a response
  type Sink[F[_], A]         = Kleisli[F, A, Option[Response]]

  // 2) Implement the following functions:

  /** A source of ints, the body of the request should be on the
   *  format:
   *
   *  "1, 2, 3, 4, 5"
   */
  def extractList[F[_]: Monad]: Source[F, List[Int]] = ???

  /** Doubles the value of each element in the list */
  def doubleElements[F[_]: Monad]: Transform[F, List[Int], List[Int]] = ???

  case class Sum[A](value: A)

  /** Folds the list into a single value */
  def foldElements[F[_]: Monad]: Transform[F, List[Int], Option[Sum[Int]]] = ???

  val badResponseBody = "Bad user input"

  /** Returns a response from a `Sum[Int]` */
  def sumToResponse[F[_]: Monad]: Sink[F, Option[Sum[Int]]] = ???

  /** A pipeline that extracts a list of integers from a request, doubles the
   *  list, takes the sum and returns a response
   */
  def sumPipeline[F[_]: Monad]: Service[F] = ???

  /** An endpoint that responds on `GET /doublesum` */
  def SumEndpoint: Service[IO] = ???

  // Bonus assignments
  // =================
  //
  // The fact that a transformation will return something can actually be
  // implemented in the context of the monad. That is, we can say that the
  // container `F` will have an `Option` inside it like:
  //
  //    F[Option[A]]
  //
  // There is a Monad for this called `OptionT`. This monad has two holes and
  // has a definition that looks like this:
  //
  //    case class OptionT[F[_], A](value: F[Option[A]]) { ... }
  //
  // Implement the previous methods, but in terms of `OptionT`:

  def foldElements2[F[_]: Monad]: Transform[OptionT[F, ?], List[Int], Sum[Int]] = ???

  def sumResponse2[F[_]: Monad]: Sink[OptionT[F, ?], Sum[Int]] = ???
}
