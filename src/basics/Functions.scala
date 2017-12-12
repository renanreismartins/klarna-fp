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

  def applyTwice[A](f: A => A): A =
    ???

  def zipBy[A, B, C](xs: List[A], ys: List[B], f: (A, B) => C): List[C] =
    ???

  def isSorted[A](xs: List[A], ordered: (A, A) => Boolean): Boolean =
    ???

  def findFirst[A](xs: List[A], p: A => Boolean): Option[A] =
    ???

  //The following functions are pretty cool - once they compile, they're
  //correct - I mean, just look at these types!

  /** Partially applies the function `f` and gives back a function from
    * `B => C`
    */
  def partial[A, B, C](a: A, f: (A, B) => C): B => C =
    ???

  /** Flips the arguments to the function `f` */
  def flip[A, B, C](f: (A, B) => C): (B, A) => C =
    ???

  /** Turns a function accepting to values into a function that accepts one
    * value, but gives back a function taking one value
    */
  def curry[A, B, C](f: (A, B) => C): A => B => C =
    ???

  /** The dual to `curry` */
  def uncurry[A, B, C](f: A => B => C): (A, B) => C =
    ???

  /** Composes the two functions together to create one super function from
    * `A => C`
    */
  def compose[A, B, C](f: B => C, g: A => B): A => C =
    ???
}
