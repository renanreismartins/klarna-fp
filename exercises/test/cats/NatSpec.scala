package klarna.fp

import org.scalatest._
import cats.data.NonEmptyList
import scala.util.Try

class NatSpec extends FlatSpec with Matchers {
  import Nat._

  "Natural transformation `Try ~> Option`" should "yield the correct result on Success" in {
    val t = Try(1)
    TryToOption(t) should be (Option(1))
  }

  it should "yield the correct result on Failure" in {
    val t = Try(throw new Exception)
    TryToOption(t) should be (None)
  }


  "Natural transformation `Validation[L, ?] ~> Either[L, ?]`" should "turn `Valid` into `Right`" in {
    val v = Valid(1)
    ValidationToEither[Throwable](v) should be (Right(1))
  }

  it should "turn `Recovered` into `Right`" in {
    val v = Recovered(1, NonEmptyList(new Exception, Nil))
    ValidationToEither[Throwable](v) should be (Right(1))
  }

  it should "turn `Unrecoverable` into `Left`" in {
    case class MyException() extends Exception("")
    val v = Unrecoverable(NonEmptyList(MyException(), Nil))
    ValidationToEither[MyException](v) should be (Left(NonEmptyList(MyException(), Nil)))
  }

}
