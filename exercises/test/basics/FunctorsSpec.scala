package klarna.fp
package basics

import org.scalatest._
import cats.tests.CatsSuite
import cats.syntax.functor._
import cats.laws.discipline.FunctorTests

import org.scalacheck.{Arbitrary, Gen}

class FunctorsSpec extends CatsSuite {

  import Functors._

  test("List Functor - identity") {
    assert(Functors.listFunctor.map(List(1, 2))(identity) == List(1, 2))
    // The identity law says that when applying the `identity` function to a
    // functor, it should return the original value:
    //
    // Implement the test here!
  }

  test("List Functor - composition") {
    val f1 = (x: Int) => x * 2
    val f2 = (x: Int) => x + 2
    val composedFunction = f2.compose(f1)

    assert(Functors.listFunctor.map(List(1, 2))(f1).map(f2) == Functors.listFunctor.map(List(1, 2))(composedFunction))

    // The composition law says:
    //
    //    It should not matter whether we map a composed function or first map
    //    one function and then the other (assuming the application order
    //    remains the same in both cases).
  }

  checkAll("List.FunctorLaws", FunctorTests[List].functor[Int, Int, String])

  // Create arbitrary instances of the Tree
  implicit def arbTree[A: Arbitrary]: Arbitrary[Tree[A]] =
    Arbitrary(Gen.oneOf(Gen.const(Empty[A]()), {
      Arbitrary.arbitrary[A].flatMap(Node(_, Empty(), Empty()))
    }))

  checkAll("Tree.FunctorLaws", FunctorTests[Tree].functor[Int, Int, String])

  test("Function Functor - identity") {
    val f: Int => Int = _ * 2

    val f2 = functionFunctor[Int].map(f)(identity)
    assert(f2(2) == 4)
  }

  test("Function Functor - composition") {
    val fa: Int => Int = -_
    val f: Int => Int = _ * 2
    val g: Int => Int = _ + 13

    assert(
      functionFunctor[Int].map(fa)(g compose f)(11) ==
        functionFunctor[Int].map(functionFunctor[Int].map(fa)(f))(g)(11)
    )
  }
}
