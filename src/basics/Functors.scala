package klarna.fp
package basics

import cats.Eq
import cats.Functor

object Functors {

  // 1) Implement the law-abiding functor for list - and implement its tests!
  implicit def listFunctor: Functor[List] = ???

  trait Tree[A]
  case class Node[A](value: A, left: Tree[A], right: Tree[A]) extends Tree[A]
  case class Empty[A]() extends Tree[A]

  object Tree {
    implicit def eqTree[A: Eq]: Eq[Tree[A]] = Eq.fromUniversalEquals

    // 2) Implement a functor for tree:
    implicit def treeFunctor: Functor[Tree] = ???
  }

  // 3) Implement a functor for a function `T => ?`:
  implicit def functionFunctor[T]: Functor[T => ?] = ???
}
