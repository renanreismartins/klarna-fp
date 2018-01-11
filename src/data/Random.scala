package klarna.fp
package data

import cats.data.State

object Random {

  // 1) Implement the `nextInt` function

  /** Gives a pseudo-random long based on the `old` seed */
  def newSeed(old: Long): Long =
    (old * 0x5DEECE66DL + 0xBL) & (~0x0L)

  /** Creates an integer from a seed */
  def intFrom(seed: Long): Int =
    (seed >>> 16).toInt

  case class Generator(seed: Long) {
    def nextInt: (Generator, Int) = ???
  }

  /** With the above in mind, we can take any stateful library and make it
   *  stateless by simply returning the state.
   *
   *  As you might imagine, this is a fairly common pattern and as such, we'd
   *  like to abstract over it. This is a PSEUDO implementation of what we'd
   *  like to say:
   *
   * {{{
   * type State[A, B] = A => (State[A, B], B)
   * }}}
   *
   * A state could simply be a function from an initial value - to a new value
   * and its state. We can't model it quite as simply as this. But there is -
   * as per usual - a monad for this.
   *
   * Enter the State monad.
   *
   * {{{
   * val doublingCounter: State[Int, Int] =
   *   State(i => (i + 1, i * 2))
   * }}}
   *
   * This creates a counter that can be run with an initial value:
   *
   * {{{
   * doublingCounter.run(1).value // res: (2, 1)
   * }}}
   *
   * The counter returns the state of the counter (2), and the current value of
   * what it is keeping the state over (1).
   *
   * Since this is a monad, we can actually use this in for comprehensions like:
   *
   * {{{
   * val doubleThrice = for {
   *   n1 <- doublingCounter
   *   n2 <- doublingCounter
   *   n3 <- doublingCounter
   * } yield n1 * n2 * n3
   *
   * val faculty3 = doubleThrice.run(1)
   * faculty3.value // res: 6
   * }}}
   */

  // 2) Implement a random number generator based on the State monad without
  //    using `Generator`

  type Rand[A] = State[Long, A]

  val nextInt: Rand[Int] = ???

  // Implement in terms of `nextLong`:
  val nextBoolean: Rand[Boolean] = ???
}
