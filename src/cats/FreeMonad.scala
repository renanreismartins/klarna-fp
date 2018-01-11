package klarna.fp

import java.io.PrintStream

import scala.collection.mutable
import scala.reflect.runtime.universe._
import scala.reflect.ClassTag

import cats.{~>, Id, InjectK}
import cats.free.Free

object FreeMonad {

  // A Free monad is essentially a structure that describes a program. It is a
  // sequence of data which can later be interpreted by natural transformations.
  //
  // This sort of sounds like dependency injection - you describe a program,
  // pass the interface, and then you make a concrete implementation of said
  // interface.
  //
  // However - free monads are data. And as such they can be treated like data,
  // each operation can be inspected and modified. They can be combined and
  // extended without needing to modify the original "interface".
  //
  // We define a set of operations as a closed GADT. That means that each
  // operation will be encoded as a subclass of the GADT. In this exercise you
  // will define operations as subclasses of `StoreOp`.
  //
  // The store is a simple key-value store that operates on keys, which are
  // strings and on `get` returns the resulting value if the key exists and the
  // type is equal to `V`.

  /** Define the operations needed for the store as subclasses of `StoreOp` */
  sealed trait StoreOp[A]

  class StoreOps[F[_]](implicit I: InjectK[StoreOp, F]) {
    def put[V](k: String, v: V): Free[F, Unit] =
      ???

    /** Gets the element of type V for key `k`, you can assume that if the key
      * exists - it will always be of type `V`
      */
    def get[V: ClassTag](k: String): Free[F, Option[V]] =
      ???

    /** Deletes an entry from the store and returns whether or not something was deleted */
    def delete(k: String): Free[F, Boolean] =
      ???

    /** Create an update by combining `get` and `put`
      *
      * @return the value being replaced if exists
      */
    def update[V](k: String, newValue: V): Free[F, Option[V]] =
      ???
  }

  object StoreOps {
    def apply[F[_]](implicit I: InjectK[StoreOp, F]) = new StoreOps[F]
  }

  /** To be able to evaluate a StoreOp program, we need an interpreter. This
    * interpreter is simply a natural transformation between the "algebra" and
    * the resulting container - most likely a Monad e.g. `Task`, `Either`,
    * `List`
    *
    * Implement `StoreId`. Remember that `Id` is simply:
    *
    *   type Id[A] = A
    *
    * Please note that in a real-life scenario, we'd have type-safety for the
    * map. But for now, you can cast `AnyRef => V`
    */
  case class StoreId() extends (StoreOp ~> Id) {
    val map = mutable.Map[String, AnyRef]()

    def apply[A](a: StoreOp[A]): Id[A] = ???
  }

  /** It's useful to be able to log the operations performed on the store. A
    * simple way to do this is to use the natural transformation defined above
    * with some added logic:
    */
  final case class StoreLog(log: PrintStream, interpreter: StoreOp ~> Id) extends (StoreOp ~> Id) {
    // Each operation should simply log the case class that it operates on in:
    def apply[A](a: StoreOp[A]): Id[A] = ???
  }
}
