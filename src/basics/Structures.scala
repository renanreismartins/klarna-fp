package klarna.fp
package basics

import cats.{Eq, Show}
import cats.implicits._

object Structures {

  sealed trait IList[A] { self =>
    /** Returns the tail of a list or `Nil` if list is empty */
    def tail: IList[A]

    /** Replaces the head of the list or returns a list of a single element if
     *  the list was empty
     */
    def setHead(a: A): IList[A] =
      self match {
        case Cons(_, tlFun) => Cons(a, tlFun())
        case Nil() => Cons(a)
      }

    /** Drop `n` elements and return the remaining list */
    def drop(n: Int): IList[A] =
      ???

    /** Drop elements while the predicate `p` yields true */
    def dropWhile(p: A => Boolean): IList[A] =
      ???

    /** Lazily appends the list `other` after `self` */
    def append(other: IList[A]): IList[A] =
      ???

    /** Applies the function to each element of the list */
    def map[B](f: A => B): IList[B] =
      ???

    /** Applies the function to each element of the list */
    def flatMap[B](f: A => IList[B]): IList[B] =
      ???

    /** `foldRight` folds a list into a single value `B` */
    def foldRight[B](z: B)(f: (A, B) => B): B =
      ???

    /** Implement `sum` in terms of `foldRight` */
    def sum(implicit N: Numeric[A]): A =
      ???

    /** Implement `product` in terms of `foldRight` */
    def product(implicit N: Numeric[A]): A =
      ???
  }

  case class Nil[A]() extends IList[A] {
    def tail: IList[A] = this
  }

  class Cons[A](h: () => A, tl: () => IList[A]) extends IList[A] {
    lazy val head: A = h()
    lazy val tail: IList[A] = tl()
  }

  object Cons {

    def apply[A](h: => A): IList[A] =
      new Cons(() => h, () => Nil[A]())

    def apply[A](h: => A, tl: => IList[A]): IList[A] =
      new Cons(() => h, () => tl)


    def unapply[A](xs: IList[A]): Option[(A, () => IList[A])] = xs match {
      case c: Cons[A] @unchecked => Option { (c.head, () => c.tail) }
      case _ => None
    }
  }

  object IList {
    implicit def eq[A: Eq] = new cats.Eq[IList[A]] {
      def eqv(a1: IList[A], a2: IList[A]): Boolean = (a1, a2) match {
        case (Nil(),         Nil()        ) => true
        case (_,             Nil()        ) => false
        case (Nil(),         _            ) => false
        case (Cons(h1, tl1), Cons(h2, tl2)) => (h1 === h2) && eqv(tl1(), tl2())
      }
    }

    implicit def show[A: Show] = new cats.Show[IList[A]] {
      def show(xs: IList[A]) = xs match {
        case Nil() => "Nil()"
        case Cons(a, tl) => s"Cons(${a.show}, ${show(tl())})"
      }
    }
  }
}

