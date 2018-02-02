package klarna.fp
package data

import cats.{Eval, Eq, Show}
import cats.implicits._

object Structures {

  sealed trait IList[A] { self =>
    /** Returns the tail of a list or `Nil` if list is empty */
    def tail: IList[A]

    /** Replaces the head of the list or returns a list of a single element if
     *  the list was empty
     */
    final def setHead(a: => A): IList[A] =
      ???

    /** Drop `n` elements and return the remaining list */
    final def drop(n: Int): IList[A] =
      ???

    /** Drop elements while the predicate `p` yields true */
    final def dropWhile(p: A => Boolean): IList[A] =
      ???

    /** Lazily appends the list `other` after `self` */
    final def append(other: => IList[A]): IList[A] =
      ???

    final def appendEl(a: => A): IList[A] = append(Cons(a))

    /** Applies the function to each element of the list */
    final def map[B](f: A => B): IList[B] =
      ???

    /** Applies the function to each element of the list */
    final def flatMap[B](f: A => IList[B]): IList[B] =
      ???

    /** `foldRight` folds a list into a single value `B`
     *
     *  @note - this is achieved using `Eval` from cats. Eval represents an
     *  evaluation of something - to be done now by calling `.value` or later
     *  by mapping and passing on.
     */
    final def foldRight[B](z: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      ???

    /** Implement `sum` in terms of `foldRight` */
    final def sum(implicit N: Numeric[A]): Eval[A] =
      ???

    /** Implement `product` in terms of `foldRight` */
    final def product(implicit N: Numeric[A]): Eval[A] =
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
      def eqv(xs: IList[A], ys: IList[A]): Boolean = (xs, ys) match {
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

