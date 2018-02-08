package klarna.fp
package data

import cats.Id
import cats.data.IndexedStateT
import org.scalatest.{FlatSpec, Matchers}

class IndexedMonadSpec extends FlatSpec with Matchers {
  import IndexedMonad._

  "shut" should "increment value with 1" in {
    shut(false).run(Shut(0))._1.times should be (1)
  }

  //it should "not increment with value 1 if jam == true" in {
  //  shut(true).run(Shut(0))._1.times should be (0)
  //}

  "open" should "increment value with 1" in {
    open(false).run(Open(0))._1.times should be (1)
  }

  //it should "not increment with value 1 if jam == true" in {
  //  open(true).run(Open(0))._1.times should be (0)
  //}

  "openShutOpen" should "increment value by 3 for a single call" in {
    openShutOpen.run(Shut(0))._1.times should be (3)
  }

  "randomly opening and closing" should "yield a random count of times operated" in {
    import Random.Generator

    val initial = Generator(252149039170011L)

    def rand[A](old: Generator): IndexedStateT[Id, A, A, (Generator, Boolean)] =
      IndexedStateT[Id, A, A, (Generator, Boolean)] { a =>
        val (gen, int) = old.nextInt
        (a, (gen, int % 2 != 0))
      }

    val state = for {
      r1 <- rand[Shut](initial)
      _  <- shut(r1._2)
      r2 <- rand[Open](r1._1)
      _  <- open(r2._2)
      r3 <- rand[Shut](r2._1)
      _  <- shut(r3._2)
      r4 <- rand[Open](r3._1)
      v  <- open(r4._2)
    } yield v

    state.run(Shut(0))._1.times should be (3)
  }
}
