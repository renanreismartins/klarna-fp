package klarna.fp
package data

import cats.implicits._
import cats.{Eq, Eval, Show}

object Structures {

  sealed trait IList[A] {
    self =>
    /** Returns the tail of a list or `Nil` if list is empty */
    def tail: IList[A]

    /** Replaces the head of the list or returns a list of a single element if
      * the list was empty
      */
    final def setHead(a: => A): IList[A] =
      Cons(a, tail)

    /** Drop `n` elements and return the remaining list */
    final def drop(n: Int): IList[A] = {
      def aux(x: IList[A], acc: Int): IList[A] = {
        if (acc == n || x.tail == Nil) x else aux(x.tail, acc + 1)
      }

      aux(this, 0)
    }

    /** Drop elements while the predicate `p` yields true */
    final def dropWhile(p: A => Boolean): IList[A] = {
      def aux(x: IList[A], p: A => Boolean): IList[A] = {
        x match {
          case Nil() => Nil()
          case Cons(a, f) => if (p(a)) aux(f(), p) else x
        }
      }

      aux(this, p)
    }

    /** Lazily appends the list `other` after `self` */
    final def append(other: => IList[A]): IList[A] = {
      this match {
        case Nil() => other
        case Cons(a, tl) => Cons(a, tl().append(other))
      }
    }

    final def appendEl(a: => A): IList[A] = append(Cons(a))

    /** Applies the function to each element of the list */
//    final def map[B](f: A => B): IList[B] = {
//      def aux(xs: IList[A], acc: => IList[B]): IList[B] = {
//        xs match {
//          case (Cons(a, _)) => aux(xs.tail, acc.append(Cons(f(a))))
//          case _ => acc
//        }
//      }
//
//      aux(this, Nil())
//    }
    final def map[B](f: A => B): IList[B] = {
      def aux(xs: IList[A]): IList[B] = {
        xs match {
          case (Cons(a, _)) => Cons(f(a), aux(xs.tail))
          case _ => Nil()
        }
      }

      aux(this)
    }

    /** Applies the function to each element of the list */
    final def flatMap[B](f: A => IList[B]): IList[B] = {
      def aux(xs: IList[A], acc: IList[B]): IList[B] = {
        xs match {
          case (Cons(a, _)) => f(a).append(acc).append(aux(xs.tail, acc))
          case _ => Nil()
        }
      }

      aux(this, Nil())
    }

    /** `foldRight` folds a list into a single value `B`
      *
      * @note - this is achieved using `Eval` from cats. Eval represents an
      *       evaluation of something - to be done now by calling `.value` or later
      *       by mapping and passing on.
      */
    final def foldRight[B](z: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = {
      def aux(xs: IList[A], acc: Eval[B]): Eval[B] = {
        xs match {
          case (Cons(a, _)) =>f(a, aux(xs.tail, z))
          case _ => acc
        }
      }

      aux(this, z)
    }
//    final def foldRight[B](z: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = {
//      def aux(xs: IList[A], acc: Eval[B]): Eval[B] = {
//        xs match {
//          case (Cons(a, _)) => aux(xs.tail, f(a, acc))
//          case _ => acc
//        }
//      }
//
//      aux(this, z)
//    }

    /** Implement `sum` in terms of `foldRight` */
    final def sum(implicit N: Numeric[A]): Eval[A] =
      foldRight(Eval.later(N.zero))((a, b) => b.map(x => N.plus(a, x)))

    /** Implement `product` in terms of `foldRight` */
    final def product(implicit N: Numeric[A]): Eval[A] =
      foldRight(Eval.later(N.one))((a, b) => b.map(x => N.times(a, x)))
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
      case c: Cons[A]@unchecked => Option {
        (c.head, () => c.tail)
      }
      case _ => None
    }
  }

  object IList {
    implicit def eq[A: Eq] = new cats.Eq[IList[A]] {
      def eqv(xs: IList[A], ys: IList[A]): Boolean = (xs, ys) match {
        case (Nil(), Nil()) => true
        case (_, Nil()) => false
        case (Nil(), _) => false
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

