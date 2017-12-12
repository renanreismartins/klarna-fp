package klarna.fp
package basics

import scala.annotation.tailrec

object Tailrecursion {

  /** Tail Recursion
    * ==============
    * In functional programming, we prefer a declarative style of
    * implementation. For instance, defining the absolute value is as simple
    * as:
    *
    * {{{
    * def abs(i: Int) = if (i < 0) -i else i
    * }}}
    *
    * this definition would look the same if we were coding in an imperative
    * style. However, when it comes to defining something like a sum - we need
    * to start dealing with recursion in order for the declarative style to hold:
    *
    * {{{
    * def sum(xs: List[Int]): Int = xs match {
    *   case x :: xs => x + sum(xs)
    *   case Nil => 0
    * }
    * }}}
    *
    * But what happens if the list is huge? We will cause a
    * `StackOverflowException`. Because of this, scala has an annotation
    * `@tailrec` that ensures that the function can be transformed by the
    * compiler in such a way that we eliminate the stack allocations.
    *
    * For this to work, the call to the recursive function must come last in
    * the definition.
    *
    * In this section, you'll implement tail recursive functions!
    */

  /** Computes the factorial of `n`
    *
    * @note Sometimes it isn't possible to make the public function
    *       tail-recursive.  In those cases, we can simply introduce a nested
    *       function
    *
    *       We prefer nested methods to help readability, this is equivalent to
    *       a private method - but it's private to the function in which it is
    *       defined. If this method were to be shared - then we would move it
    *       out. Of course, like with all things programming - taste is the
    *       ultimate deciding factor :)
    */
  def factorial(n: Int): Int = {
    //@tailrec
    def loop(n: Int, acc: Int): Int = ???

    ???
  }

  /** OPTIONAL - this one is a bit tricky to get tail-recursive
    *
    * Computes the `n`th number of the fibonacci series:
    *
    *   0, 1, 1, 2, 3, 5, 8, 13, ...
    */
  def fib(n: Int): Int = ???
}
