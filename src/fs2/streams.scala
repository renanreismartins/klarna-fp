package klarna.fp

import fs2.Stream
import fs2.Pure

object Streams {

  /** Building streams */
  def singleElement[A](a: A): Stream[Pure, A] =
    ???

  /** Creates a stream which repeats the element `a` */
  def repeated[A](a: A): Stream[Pure, A] =
    ???

  /** Combines two streams so that all the elements from `s1` preceed `s2` */
  def concatenate[A](s1: Stream[Pure, A], s2: Stream[Pure, A]): Stream[Pure, A] =
    ???

  /** Returns a list with only even elements */
  def even(s: Stream[Nothing, Int]): Stream[Pure, Int] =
    ???

  /** Returns a sum of all elements in the stream */
  def sum[A: Numeric](s: Stream[Nothing, A]): Stream[Pure, A] =
    ???

  /** Combines two streams so that every other element (in the synchronous
    * case) is belongs to `s1` and `s2` respectively
    *
    * Ergo if the streams were lists, this would be the expected behaviour:
    * {{{
    * val xs1 = List(1, 2)
    * val xs2 = List(3, 4)
    *
    * combine(xs1, xs2) == List(1, 2, 3 ,4)
    * }}}
    *
    */
  def combine[A](s1: Stream[Pure, A], s2: Stream[Pure, A]): Stream[Pure, A] =
    ???

  /** Calculate the sum of two streams of integers */
  def sum[A: Numeric](s1: Stream[Pure, A], s2: Stream[Pure, A]): Stream[Pure, A] =
    ???

  /** Given an initial value 'z' create a stream that starts with that value
    * and then applies the function `f` iteratively to each element. So as to
    * unfold a single value to a stream of values.
    *
    * For example:
    * {{{
    * unfold(0, _ + 1).take(4).toList == List(0, 1, 2, 3)
    * }}}
    */
  def unfold[A](z: A)(f: A => A): Stream[Pure, A] =
    ???

  /** A function that takes a stream of List[Int] and returns a flattened
    * version of the list
    *
    * If this were lists, the following would be expected:
    * {{{
    * val xs = List(List(1, 2), List(3, 4))
    * flatten(xs) == List(1, 2, 3, 4)
    * }}}
    */
  def flatten[A](s: Stream[Pure, List[A]]): Stream[Pure, A] =
    ???

  /** Intersperse should inject the `i` value into the stream, such that every
    * other value is `i`.
    *
    * Example using lists:
    * {{{
    * val xs = List(1, 2, 3)
    * intersperse(xs, 0) == List(1, 0, 2, 0, 3)
    * }}}
    */
  def intersperse[A](s: Stream[Pure, A], i: A): Stream[Pure, A] =
    ???
}
