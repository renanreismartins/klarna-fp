package klarna.fp
package basics

import org.scalatest._

class TailRecursionSpec extends FlatSpec with Matchers {

  import Tailrecursion._

  "factorial" should "produce the correct value for n = 0" in {
    factorial(0) shouldEqual (1)
  }

  it should "produce the correct value for n = 1" in {
    factorial(1) shouldEqual (1)
  }

  it should "produce the correct value for n = 2" in {
    factorial(2) shouldEqual (2)
  }

  it should "produce the correct value for n = 3" in {
    factorial(3) shouldEqual (6)
  }

  it should "produce the correct value for n = 4" in {
    factorial(4) shouldEqual (24)
  }

  it should "produce the correct value for n = 5" in {
    factorial(5) shouldEqual (120)
  }

  it should "produce the correct value for n = 6" in {
    factorial(6) shouldEqual (720)
  }

  "fib" should "produce the correct value for n = 0" in {
    fib(0) shouldEqual (0)
  }

  it should "produce the correct value for n = 1" in {
    fib(1) shouldEqual (1)
  }

  it should "produce the correct value for n = 2" in {
    fib(2) shouldEqual (1)
  }

  it should "produce the correct value for n = 3" in {
    fib(3) shouldEqual (2)
  }

  it should "produce the correct value for n = 4" in {
    fib(4) shouldEqual (3)
  }

  it should "produce the correct value for n = 5" in {
    fib(5) shouldEqual (5)
  }

  it should "produce the correct value for n = 6" in {
    fib(6) shouldEqual (8)
  }

  it should "produce the correct value for n = 7" in {
    fib(7) shouldEqual (13)
  }

  it should "produce the correct value for n = 16" in {
    fib(16) shouldEqual (987)
  }


}
