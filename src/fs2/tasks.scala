package klarna.fp

import fs2.{ Strategy, Task }
import fs2.interop.cats._

object Tasks {
  final implicit val S: Strategy =
    Strategy.fromFixedDaemonPool(5, threadName = "fp-klarna-worker")

  /** Creates a Task that is completed immediately */
  def immediately[A](a: => A): Task[A] =
    ???

  /** Creates a task that is not evaluated immediately */
  def later[A](a: => A): Task[A] =
    ???

  /** Creates a task that lifts the computation into an either - catching
    * exceptions in the left part of the disjunction.
    */
  def safeTask[A](task: Task[A]): Task[Either[Throwable, A]] =
    ???

  /** If the first task `t1` throws an exception, the task `t2` is returned in
    * its stead
    */
  def recover[A, B >: A](t1: Task[A])(t2: Task[B]): Task[B] =
    ???

  /** Ensures the result of the task conforms to the predicate and otherwise
    * fails the task
    */
  def ensure[A](t: Task[A])(p: A => Boolean, ex: => Throwable): Task[A] =
    ???

  /** Combines two tasks in a tuple */
  def product[A, B](t1: Task[A], t2: Task[B]): Task[(A, B)] =
    ???

  /* Flattens a list of tasks to a task of list */
  def flatten[A](xs: List[Task[A]]): Task[List[A]] =
    ???

  /** Traverse a list of tasks into a task of list where the function has been
    * applied to each element
    */
  def traverse[A, B](xs: List[Task[A]])(f: A => Task[B]): Task[List[B]] =
    ???
}
