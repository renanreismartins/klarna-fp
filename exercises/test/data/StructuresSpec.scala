package klarna.fp
package data

import org.scalatest.FlatSpec

import cats.{Id, Eval, Eq, Show}
import cats.implicits._

class StructuresSpec extends FlatSpec {
  import Structures._

  val cons3 = Cons(1, Cons(2, Cons(3)))

  def assertEqv[F[_], A](xs: F[A], ys: F[A])(
    implicit
    E: Eq[F[A]],
    S: Show[F[A]],
  ) =
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

  it should "be lazy in its execution" in {
    var wasLazy = true
    val c1 = new Cons(() => 1, () => {
      wasLazy = false
      Cons(2)
    })

    c1.append(Cons(3))
    assert(wasLazy, "Append was not lazy in its append action")
  }

  "IList#map" should "transform a list successfully" in {
    assertEqv(Nil[Int]().map(_.toString), Nil[String]())
    assertEqv(cons3.map(_.toString), Cons("1", Cons("2", Cons("3"))))
    assertEqv(cons3.map(_ * 2), Cons(2, Cons(4, Cons(6))))
  }

  it should "be lazy" in {
    var wasLazy = true
    val c1 = new Cons(() => 1, () => {
      wasLazy = false
      Cons(2)
    })

    c1.map(_ * 2)

    assert(wasLazy, "Append was not lazy in its append action")
  }

  // Bonus, have a look at how `unapply` is implemented. Using the same type of
  // matching you should be able to make the mapping of the head lazy as well
  ignore should "be lazy in mapping head" in {
    var wasLazy = true
    val c1 = new Cons(() => {
      wasLazy = false
        1
    }, () => {
      wasLazy = false
      Cons(2)
    })

    c1.map(_ * 2)

    assert(wasLazy, "Append was not lazy in its append action")
  }

  "IList#flatMap" should "transform a list successfully" in {
    assertEqv(Nil[Int]().flatMap(x => Cons(x)), Nil[Int]())
    assertEqv(Nil[Int]().flatMap(x => Cons(x.toString)), Nil[String]())

    val xxs = Cons(Cons(1), Cons(Cons(2), Cons(Cons(3))))
    assertEqv(xxs.flatMap(identity), cons3)
  }

  it should "be lazy in its execution" in {
    var wasLazy = true
    val xxs: IList[IList[Int]] = new Cons(() => Cons(1), () => {
      wasLazy = false
      Cons(Cons(2))
    })

    xxs.flatMap(identity)

    assert(wasLazy, "flatMap failed to be lazy in its execution")
  }

  "IList#foldRight" should "return the correct value for addition" in {
    val i: Int = cons3.foldRight(Eval.now(0))((a, ev) => ev.map(_ + a)).value
    assertEqv[Id, Int](i, 6)
  }

  it should "return the correct value for multiplication" in {
    val i: Int = cons3.foldRight(Eval.now(2))((a, ev) => ev.map(_ * a)).value
    assertEqv[Id, Int](i, 12)
  }

  it should "fold in the correct direction" in {
    val xs = cons3.foldRight(Eval.now(Nil[Int](): IList[Int]))((a, ev) => ev.map(xs => Cons(a, xs))).value
    assertEqv(xs, cons3)
  }

  it should "be lazy in its evaluation" in {
    var wasLazy = true
    cons3.foldRight(Eval.later {
      wasLazy = false
      1
    })((a, ev) => ev.map(_ * a))

    assert(wasLazy, "flatMap failed to be lazy in its execution")
  }

  "IList#sum" should "yield the correct answer" in {
    assertEqv[Id, Int](cons3.sum.value, 6)
    assertEqv[Id, Int](cons3.appendEl(4).sum.value, 10)
  }

  "IList#product" should "yield the correct answer" in {
    assertEqv[Id, Int](cons3.product.value, 6)
    assertEqv[Id, Int](cons3.appendEl(4).product.value, 24)
  }

}
