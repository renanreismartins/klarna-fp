package klarna.fp
package data

import cats.Id
import cats.data.IndexedStateT

object IndexedMonad {

  // An indexed monad is a monad that takes *two* extra type parameters in
  // order to be able to keep track of some form of state transition.
  //
  // Let's use this in order to model a door. For now the door has two states:
  //
  // Open <-> Closed
  //
  // It's a bit of a special door, because it actually counts the amount of
  // times that it has been either opened or closed.
  //
  // As you may have surmised, one such monad that does this is the
  // `IndexedStateT` monad. This monad is defined as:
  //
  // ```
  // final case class IndexedStateT[F[_], SA, SB, A](runF: F[SA => F[(SB, A)]])
  // ```
  //
  // A declaration like:
  //
  // ```
  // type Transition[A, B] = IndexedStateT[Id, A, B, Unit]
  // ```
  //
  // can be read as "Transition from A to B is an IndexedStateT which gives
  // back unit in the state `A` and can be transitioned into the state `B`"
  //
  // This may seem like a mouth-ful, and of course we can simply read it as
  // "state from A to B returning Unit". The first explanation, however, should
  // be the all-encompassing one.

  trait Counter {
    def times: Int
  }

  trait Openish
  case class Open(times: Int) extends Openish with Counter

  trait Shutish
  case class Shut(times: Int) extends Shutish with Counter

  // 1) Implement the methods below:

  /** Opens the door and increments the contained value by 1 if `jam` is false */
  def shut(jam: Boolean): IndexedStateT[Id, Shut, Open, Unit] =
    ???

  /** Shuts the door and increments the contained value by 1 if `jam` is false */
  def open(jam: Boolean): IndexedStateT[Id, Open, Shut, Unit] =
    ???

  /** Shuts an open door, then open it, then shut it again! (No jamming) */
  def shutOpenShut: IndexedStateT[Id, Shut, Open, Unit] =
    ???

  // 2) Add two states: `JammedOpen` and `JammedShut`

  // 3) Change the signature of `open`, `shut`, `openShutOpen, to accommodate
  //    for the new states

  // 4) Uncomment the commented tests in `IndexedMonadSpec`. Change the
  //    implementations of `open` and `shut` to only change state if `jam` is
  //    true
}
