package klarna.fp

import fs2.Stream

object Streams {

  /** Building streams */

  /** Returns a list with only even elements */
  def even(s: Stream[Nothing, Int]): Stream[Nothing, Int] =
    s.filter(_ % 2 == 0)

  /** Returns a sum of all elements in the stream */
  def sum(s: Stream[Nothing, Int]): Stream[Nothing, Int] =
    s.fold(0)(_ + _)

  def combine(s1: Stream[Nothing, Int], s2: Stream[Nothing, Int]): Stream[Nothing, Int] =
    s1.interleave(s2)

  def sum(s1: Stream[Nothing, Int], s2: Stream[Nothing, Int]): Stream[Nothing, Int] =
    sum(combine(s1, s2))

  def unfold(z: Int, f: Int => Int): Stream[Nothing, Int] =
    Stream(z).repeat.mapAccumulate(0) {
      case (p, _) =>
        val c1 = f(p)
        (c1, p)
    }
    .map(_._1)

  /** A function that takes a stream of List[Int] and returns a flattened
    * version of the list
    */
  def flatten(s: Stream[Nothing, List[Int]]): Stream[Nothing, Int] =
    s.flatMap { x => Stream.emits(x) }
}
