package klarna.fp
package basics

import org.scalatest.FlatSpec

import cats.{Eq, Show}
import cats.implicits._

class TestStructures extends FlatSpec {
  import Structures._

  val cons3 = Cons(1, Cons(2, Cons(3)))

  def assertEqv[A: Show](xs: IList[A], ys: IList[A])(implicit E: Eq[IList[A]]) =
    assert(E.eqv(xs, ys), show"Lists were not equivalent: $xs, $ys")

  "IList#tail" should "return correct tail" in {
    assertEqv(Cons(1).tail, Nil[Int]())
  }

  "IList#setHead" should "return correct list" in {
    assertEqv(cons3.setHead(0), Cons(0, Cons(2, Cons(3))))
  }

  it should "set head of empty list" in {
    assertEqv(Nil[Int]().setHead(1), Cons(1))
  }

  "IList#drop" should "drop the correct number of elements" in {
    assertEqv(cons3.drop(3), Nil[Int]())
  }

  it should "be able to drop more elements than the list contains" in {
    assertEqv(cons3.drop(10), Nil[Int]())
  }

  "IList#dropWhile" should "drop while the predicate is true" in {
    assertEqv(cons3.dropWhile(_ => true), Nil[Int]())
  }

  it should "stop dropping when the predicate turns false" in {
    assertEqv(cons3.dropWhile(_ => false), cons3)
    assertEqv(cons3.dropWhile(_ < 2), cons3.tail)
  }

  "IList#append" should "create the correct list" in {
    assertEqv(cons3.append(Cons(4)), Cons(1, Cons(2, Cons(3, Cons(4)))))
    assertEqv(Nil[Int]().append(Cons(1)), Cons(1))
    assertEqv(Cons(1).append(Cons(2)), Cons(1, Cons(2)))
  }

  it should "be lazy in its append action" in {
    var wasLazy = true
    val c1 = new Cons(() => 1, () => {
      wasLazy = false
      Cons(2)
    })

    c1.append(Cons(3))
    assert(wasLazy, "Append was not lazy in its append action")
  }

  "IList#map" should "transform a list successfully" in {
    ???
  }
}
