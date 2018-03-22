package fs2

import cats.effect.{Async, IO}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.FiniteDuration

object Scheduling {

  // The FS2 Scheduler helps you introduce delays into your streams

  // Note: a `Pipe` is simply a `Stream[F,O1] => Stream[F,O2]`, that is a stream transformer.

  /**
    * Apply an `eval` function to a `Stream[IO,Int]` that might fail (producing failed IO).
    * Upon failure the `eval` function should be retried a number of times (`noAttempts`),
    * before stopping the Stream with a `Left`.
    * To increase our chances, a `delay` should be introduced between retries.
    *
    * E.g:
    *   - eval is (+ 1) and succeeds consistently: Stream(1, 2, 3) --> Stream(Right(2), Right(3), Right(4))
    *   - eval is failing consistently: Stream(1, 2) --> Stream(Left("some failure"))
    *   - eval fails on the second element, noAttempts is 2, delay is 1 second:
    *         Stream(1, 2)
    *           -> eval(1) => Right(1)
    *           -> eval(2) => Left("some failure")
    *           ->   -- wait 1 second, 1 attempt left --
    *           -> eval(2) => Left("some failure")
    *           ->   -- wait 1 second, 0 attempt left --
    *           -> eval(2) => Left("some failure")
    *
    *         => return Stream(Right(1), Left("some failure"))
    */
  def retry(eval: Int => IO[Int],
            noAttempts: Int,
            delay: FiniteDuration): Pipe[IO, Int, Either[Throwable,Int]] =
    ???
}
