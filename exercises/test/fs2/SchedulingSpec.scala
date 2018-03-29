package fs2

import java.util.concurrent.atomic.AtomicInteger

import cats.effect.IO
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration.{Duration, DurationInt, DurationLong}
import scala.concurrent.ExecutionContext

class SchedulingSpec extends FlatSpec with Matchers {
  import Scheduling._

  implicit val ec: ExecutionContext = ExecutionContext.global

  "Retry" should "simply apply a function if no failures are detected" in {
    def eval(i: Int): IO[Int] = IO { i + 1 }
    val (result, duration) = time {
      Stream(1,2,3).covary[IO].through(retry(eval, 2, 15.second)).compile.toList.unsafeRunSync()
    }
    assert(result === List(Right(2),Right(3),Right(4)))
    assert(duration < 10.second)
  }

  it should "retry and wait a number of times before giving up" in {
    case object EvalFailure extends RuntimeException("eval-failure")

    val initialFailures = 2
    val noAttemps = initialFailures + 1

    val failures = new AtomicInteger(initialFailures)

    def eval(i: Int): IO[Int] = IO {
      println(s"Eval $i - " + Thread.currentThread())
      if (i > 2) {
        if (failures.getAndDecrement() > 0)
          throw EvalFailure
        else
          failures.set(noAttemps + 1)
      }
      i * 2
    }

    val delay = 500.millis

    val (result, duration) = time {
      Stream(1, 2, 3, 4, 5).covary[IO].through(retry(eval, noAttemps, delay)).compile.toList.unsafeRunSync()
    }

    val expectedNoDelay = initialFailures + noAttemps

    assert(result === List(Right(2), Right(4), Right(6), Left(EvalFailure)))
    assert(duration >= (expectedNoDelay * delay))
  }

  private def time[A](thunk: => A): (A, Duration) = {
    val startTime = System.currentTimeMillis()
    val result = thunk
    val duration = (System.currentTimeMillis() - startTime).millis
    (result, duration)
  }
}
