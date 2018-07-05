package klarna.fp
package basics

import cats.Eq
import cats.Functor

object Functors {

  // 1) Implement the law-abiding functor for list - and implement its tests!
  implicit def listFunctor: Functor[List] = new Functor[List] {
    override def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(a => f(a))
  }

  trait Tree[A]
  case class Node[A](value: A, left: Tree[A], right: Tree[A]) extends Tree[A]
  case class Empty[A]() extends Tree[A]

  object Tree {
    implicit def eqTree[A: Eq]: Eq[Tree[A]] = Eq.fromUniversalEquals

    // 2) Implement a functor for tree:
    implicit def treeFunctor: Functor[Tree] = new Functor[Tree] {

      override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
        case Node(a, l, r) => Node(f(a), map(l)(f), map(r)(f))
        case Empty() => Empty()
      }
    }

  }

  // 3) Implement a functor for a function `T => ?`:
  implicit def functionFunctor[T]: Functor[T => ?] = new Functor[T => ?] {
    override def map[A, B](fa: T => A)(f: A => B): T => B = (t: T) => f(fa(t))
  }

}
