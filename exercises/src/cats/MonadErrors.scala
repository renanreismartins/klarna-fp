package cats

import cats.effect.IO
import cats.data.EitherT
import cats.implicits._

object MonadErrors {

  // When talking about MonadError we're really talking about both `MonadError`
  // and `ApplicativeError` - since we already know that Monads are
  // Applicatives, for the remainder of this introduction keep that in mind.
  //
  // Let's say we want to add two numbers together that are derived from
  // Either:
  //
  //   val add: Either[Throwable, Int] = for {
  //     i <- Either.right(1)
  //     _ <- Either.left(new Exception("oh noes!"))
  //     j <- Either.right(2)
  //   } yield i + j
  //
  //   add.fold(_ => 1, identity)
  //
  // Having code like this looks fine - and for sure it is. But once you start
  // adding another level of indirection - let's say asynchronicity. We have to
  // add EitherT or OptionT:
  //
  // Now we have to do something like this:
  //
  //   val add: EitherT[IO, Throwable, Int] = for {
  //     i <- EitherT.fromEither(1)
  //     _ <- EitherT.leftT[IO, Int](new Exception("oh noes!"))
  //     j <- EitherT.rightT[IO, Throwable](2)
  //   } yield i + j
  //
  //   add.value.unsafeRunSync().fold(_ => 1, identity)
  //
  // Now we have to fiddle with type params! It would've worked fine if we only
  // had method calls in the for-comprehension, eg:
  //
  //   val add: EitherT[IO, Throwable, Int] = for {
  //     i <- getI
  //     _ <- leftException
  //     j <- getJ
  //   } yield i + j
  //
  // But even though we have this - we're stuck deconstructing and constructing
  // an `EitherT` on each call in the for-comprehension.
  //
  // What if we have the following modules:
  //
  // +-----+     +------------+     +-------------+     +-----------+     +----------+
  // | req | --> | auth check | --> | calculation | --> | transform | --> | make res |
  // +-----+     +------------+     +-------------+     +-----------+     +----------+
  //
  // if instead - each module provided a custom error type - we could have a
  // handler in `make res` to deal with these errors without needing all the
  // transformations between calls. For example, with `EitherT` we'd write
  // something like this:
  //
  //   def result[F[_]](user: User): EitherT[F, Throwable, Response] =
  //     for {
  //       _    <- EitherT.fromEither[F](auth(user))
  //       calc <- EitherT.rightT[F, Throwable](performCalc(1, 2))
  //       res  <- EitherT.fromEither[F](createResponse(calc))
  //     } yield res
  //
  //
  // but we'd much rather write:
  //
  //   def result[F[_]](user: User): F[Response] =
  //     for {
  //       _    <- auth(user)
  //       calc <- performCalc(1, 2)
  //       res  <- createResponse(calc)
  //     } yield res
  //
  // `MonadError` provides exactly the functionality to deal with this.
  //
  // Using `MonadError` we can write:
  //
  //   def result[F[_]: MonadError[Throwable, ?]](user: User): F[Response] = {
  //     val doCalc = for {
  //       _    <- auth(user)
  //       calc <- performCalc(1, 2)
  //       res  <- createResponse(calc)
  //     } yield res
  //
  //     doCalc.handleError {
  //       case AuthError(_) => Forbidden("user not authed")
  //       case CalcError(_) => InternalServerError()
  //     }
  //   }
}
