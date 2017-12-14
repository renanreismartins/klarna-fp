package klarna.fp
package basics

import org.scalatest._

class TestFunctions extends FlatSpec with Matchers {
  import Functions._

  "applyTwice" should "return the correct value for _ * 2" in {
    applyTwice((x: Int) => x * 2)(2) shouldEqual 8
  }

  "zipBy" should "create a list of tuples for identity" in {
    zipBy(List(1,3,5), List(2, 4, 6), (i: Int, j: Int) => (i, j)) shouldEqual List((1,2), (3,4), (5,6))
  }

  "isSorted" should "return true for sorted list" in {
    isSorted(List.range(0, 10), (i0: Int, i1: Int) => i0 < i1) should be (true)
  }

  it should "return false for non-sorted list" in {
    isSorted(List(1, 3, 5, 0), (i0: Int, i1: Int) => i0 < i1) should be (true)
  }
}