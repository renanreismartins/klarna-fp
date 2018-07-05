package klarna.fp

import cats.effect.IO
import scala.concurrent.ExecutionContext

object Effect {
  final implicit val EC: ExecutionContext = ExecutionContext.global

  /** Creates an IO that is not evaluated immediately */
  def later[A](a: => A): IO[A] = IO(a)

  /** Creates a task that lifts the computation into an either - catching
    * exceptions in the left part of the disjunction.
    */
  def safeIO[A](a: IO[A]): IO[Either[Throwable, A]] =
    a.attempt

  /** If the first IO `t1` throws an exception, the IO `t2` is returned in
    * its stead
    */
  def recover[A, B >: A](t1: IO[A])(t2: IO[B]): IO[B] = {
    t1.attempt.flatMap {
      case Left(_) => t2
      case Right(a) => IO.pure(a)
    }
  }

  /** Ensures the result of the effect conforms to the predicate and otherwise
    * fails the IO operation
    */
  def ensure[A](t: IO[A])(p: A => Boolean, ex: => Throwable): IO[A] =
    t.flatMap(r => if (p(r)) IO.pure(r) else IO.raiseError(ex))

  /** Combines two IOs in a tuple */
  def product[A, B](t1: IO[A], t2: IO[B]): IO[(A, B)] =
    t1.flatMap(a => t2.map(b => (a, b)))

  /* Flattens a list of IO to an IO of a list */
  def flatten[A](xs: List[IO[A]]): IO[List[A]] =
    xs.foldLeft(IO(List.empty[A]))((acc: IO[List[A]], io: IO[A]) => acc.flatMap(l => io.map(a => l :+ a)))

  /** Traverse a list of IO into an IO of list where the function has been
    * applied to each element
    */
  def traverse[A, B](xs: List[IO[A]])(f: A => IO[B]): IO[List[B]] = {
    val a: List[IO[B]] = xs.map((io: IO[A]) => io.flatMap((a: A) => f(a)))
    flatten(a)
  }
}
