package klarna.fp

import cats.effect.{Async, IO}
import fs2.{Scheduler, Stream}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.FiniteDuration

object Scheduling {

  def retry(eval: Int => IO[Int], noAttempts: Int, delay: FiniteDuration): Pipe[IO, Int, Either[Throwable,Int]] =
    source => {
      // let's get a managed scheduler first
      Scheduler[IO](corePoolSize = 1).flatMap { sched =>

        source.flatMap { i =>
          // `attempt` allows us to handle potential errors, by turning
          // a `Stream[IO, A]` into a `Stream[IO, Either[Throwable, A]]`
          val s: Stream[IO, Either[Throwable, Int]] = Stream.eval(eval(i)).attempt

          def attempt(remaining: Int): Stream[IO, Int] = s.flatMap {
            case Left(e) =>
              if (remaining > 0) {
                val print = Stream.eval(IO {
                  println(s"Failed to evaluate (${e.getMessage}), trying again in $delay. $remaining")
                })
                val sleep = sched.sleep[IO](delay)(Async[IO], ExecutionContext.global)

                // `drain` is needed as we don't want to emit `()`. The type-checker is
                // actually helping here, as combining a `Stream[IO, Unit]` with a
                // `Stream[IO, Int]` would produce a `Stream[IO, Any]`, which is not
                // what this function is supposed to return.
                //
                // and we recurse!
                print.drain ++ sleep.drain ++ attempt(remaining - 1)
              } else throw e // rethrow the exception, this will interrupt the stream

            case Right(i) =>
              Stream(i)
          }

          attempt(noAttempts)
        }
      }.attempt // to avoid a failing stream
    }
}
