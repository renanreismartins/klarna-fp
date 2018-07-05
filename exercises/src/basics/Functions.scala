package klarna.fp
package basics

object Functions {

  /** In functional programming, functions are first class. This means that we
    * can pass them around as values.
    *
    * Higher order functions (HOFs) are functions that take other functions as
    * input - or return functions.
    *
    * In order to reason about this, we first have to talk about syntax in
    * Scala.
    *
    * Let's define a function that doubles its input:
    * {{{
    * val double: Int => Int = x => x * 2
    * // we can now call this function using:
    * double(1024)
    * }}}
    *
    * But, what is really going on here? Actually, `Int => Int` is just
    * syntactic sugar for:
    *
    * {{{
    * trait Function1[Int, Int] {
    *   def apply(x: Int): Int
    * }
    * }}}
    *
    * which means that our first code-snippet, actually looks like this:
    *
    * {{{
    * val double: Function1[Int, Int] = new Function1[Int, Int] {
    *   def apply(x: Int) = x * 2
    * }
    * double.apply(1024)
    * }}}
    *
    * after desugaring. This means, that when we do:
    *
    * {{{
    * def foo[A, B](f: A => B): B
    * // we actually get:
    * def foo[A, B](f: Function1[A, B]): B
    * }}}
    */

  //The following functions are pretty cool - once they compile, they're
  //correct - I mean, just look at these types!

  /** Partially applies the function `f` and gives back a function from
    * `B => C`
    */
  def partial[A, B, C](a: A, f: (A, B) => C): B => C =
    b => f(a, b)

  /** Flips the arguments to the function `f` */
  def flip[A, B, C](f: (A, B) => C): (B, A) => C =
    (b, a) => f(a, b)

  /** Turns a function accepting to values into a function that accepts one
    * value, but gives back a function taking one value
    */
  def curry[A, B, C](f: (A, B) => C): A => B => C =
    (a) => (b) => f(a, b)

  /** The dual to `curry` */
  def uncurry[A, B, C](f: A => B => C): (A, B) => C =
    (a, b) => f(a)(b)

  /** Composes the two functions together to create one super function from
    * `A => C`
    */
  def compose[A, B, C](f: B => C, g: A => B): A => C =
    a => f(g(a))

  // These ones, however, require tests in order to verify their correctness:

  /** Yields a function that gives back the result of applying `f` twice */
  def applyTwice[A](f: A => A): A => A =
    a => f(f(a))

  /** Creates a `List[C]` by applying the function `f` to the nth element in
    * both lists.
    *
    * If the lists are not of equal size, the returned list should be as long
    * as the shortest list
    */
  def zipBy[A, B, C](xs: List[A], ys: List[B], f: (A, B) => C): List[C] =
    (xs, ys) match {
      case (x :: xs, y :: ys) => f(x, y) :: zipBy(xs, ys, f)
      case (Nil, _) => Nil
      case (_, Nil) => Nil
    }

  /** Returns true if the list is sorted in accordance to `ordered` */
  def isSorted[A](xs: List[A], ordered: (A, A) => Boolean): Boolean =
    xs match {
      case (Nil) => true
      case (_ :: Nil) => true
      case (x :: xs) => if (ordered(x, xs.head)) isSorted(xs, ordered) else false
    }

  /** Finds the first element in `xs` for which `p` returns true */
  def findFirst[A](xs: List[A], p: A => Boolean): Option[A] =
    xs match {
      case (x :: xs) => if (p(x)) Some(x) else findFirst(xs, p)
      case _ => None
    }
}
