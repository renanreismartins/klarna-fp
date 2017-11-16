package klarna.fp

import fs2.Stream
import fs2.Pure

object Streams {

  // Building streams
  // ================
  // This first section focuses on using functions available in the `Stream`
  // object and type, as such, use the obvious defined methods if applicable!

  /** Creates a stream which emits a single element */
  def singleElement[A](a: A): Stream[Pure, A] =
    ???

  /** Creates a stream which emits all elements in `xs` */
  def multipleElements[A](xs: A*): Stream[Pure, A] =
    ???

  /** Creates a stream which emits all elements in `xs` */
  def streamFromList[A](xs: List[A]): Stream[Pure, A] =
    ???

  /** Creates a stream which emits all elements in `xxs` in order */
  def streamFromLists[A](xxs: List[A]*): Stream[Pure, A] =
    ???

  /** Combines two streams so that all the elements from `s1` preceed `s2` */
  def concatenate[A](s1: => Stream[Pure, A], s2: => Stream[Pure, A]): Stream[Pure, A] =
    ???

  // Manipulating streams
  // ====================
  // In this section we focus on manipulating streams. Here, we don't want you
  // to use the built in functions with the same name if they already exist -
  // but instead either rely on the methods defined above or the combinator
  // methods on the `Stream` type

  /** Creates a stream which repeats the element `a` */
  def repeated[A](a: A): Stream[Pure, A] =
    ???

  /** Returns a list with only even elements */
  def even(s: Stream[Nothing, Int]): Stream[Pure, Int] =
    ???

  /** Returns a sum of all elements in the stream */
  def sum[A: Numeric](s: Stream[Pure, A]): Stream[Pure, A] =
    ???

  /** Combines two streams so that every other element (in the synchronous
    * case) is belongs to `s1` and `s2` respectively
    *
    * Ergo if the streams were lists, this would be the expected behaviour:
    * {{{
    * val xs1 = List(1, 3)
    * val xs2 = List(2, 4)
    *
    * combine(xs1, xs2) == List(1, 2, 3 ,4)
    * }}}
    *
    */
  def interleave[A](s1: Stream[Pure, A], s2: Stream[Pure, A]): Stream[Pure, A] =
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
