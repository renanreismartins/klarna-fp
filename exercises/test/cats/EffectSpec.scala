package klarna.fp

import org.scalatest._
import cats.effect.IO

class EffectSpec extends FlatSpec with Matchers { self =>

  "`later`" should "not evaluate side effects without being called" in {
    var gotCalled = false
    lazy val x = {
      gotCalled = true
      1
    }

    Effect.later(x)
    assert(!gotCalled, "side effects were evaluated")
  }

  it should "evaluate effects when called" in {
    var gotCalled = false
    lazy val x = {
      gotCalled = true
      1
    }

    Effect.later(x).unsafeRunSync()
    assert(gotCalled, "side effects were not evaluated")
  }

  "`safeIO`" should "catch exceptions" in {
    Effect.safeIO(
      Effect.later(throw new Exception)).unsafeRunSync()
  }

  "`recover`" should "catch exceptions" in {
    val t =
      Effect.recover(Effect.later[Int](throw new Exception))(Effect.later(1))

    t.unsafeRunSync() should be (1)
  }

  it should "not evaluate the first or second task" in {
    var didRun = false
    val t1 = Effect.later[Int](throw new Exception)
    val t2 = Effect.later {
      didRun = true
      1
    }

    Effect.recover(t1)(t2)
    assert(!didRun, "did evaluate second task in `recover`")
  }

  "`ensure`" should "not evaluate task without it being run" in {
    Effect.ensure(Effect.later(1))(_ != 1, new Exception)
  }

  it should "should throw when the predicate is true" in {
    assertThrows[Exception] {
      Effect.ensure(Effect.later(1))(_ != 1, new Exception).unsafeRunSync()
    }
  }

  it should "should not throw when the predicate is false" in {
    Effect.ensure(Effect.later(1))(_ == 1, new Exception).unsafeRunSync()
  }

  "`product`" should "not execute the Effect first" in {
    var didRun = false
    val t1 = Effect.later {
      didRun = true
      1
    }

    val t2 = Effect.later {
      didRun = true
      "two"
    }

    Effect.product(t1, t2)
    assert(!didRun, "product executed Effect before producing tuple")
  }

  "`traverse`" should "not execute immediately" in {
    Effect.flatten {
      List(
        Effect.later(1),
        Effect.later(throw new Exception),
      )
    }
  }

  it should "not catch exceptions when run" in {
    assertThrows[Exception] {
      Effect.flatten {
        List(
          Effect.later(1),
          Effect.later(throw new Exception),
        )
      }
      .unsafeRunSync()
    }
  }

  it should "evaluate Effect in parallel" in {
    var t1Done = false
    var t1Started = false
    var t2Started = false

    val t1 = Effect.later {
      self.synchronized {
        t1Started = true
        notifyAll()
        self.wait(1000)
      }
      assert(t2Started,
        "threads didn't execute out of order, second thread not started")
      t1Done = true
    }
    val t2 = Effect.later {
      self.synchronized {
        if (!t1Started) self.wait(10000)
        assert(t1Started,
          "threads didn't execute out of order, first thread not started")
        t2Started = true
        self.wait(2000)
      }
      assert(t1Done, "threads didn't execute out of order")
    }

    Effect.flatten(List(t1, t2)).unsafeRunSync()
  }

  "`traverse`" should "produce the correct result" in {
    val xs = List(Effect.later(1), Effect.later(2), Effect.later(3))

    Effect.traverse(xs)(a => Effect.later(a + 1))
      .unsafeRunSync() shouldEqual List(2, 3, 4)
  }

  it should "not evaluate Effect before being run" in {
    val xs = List(Effect.later(1), Effect.later(throw new Exception), Effect.later(3))
    Effect.traverse(xs)(a => Effect.later(a + 1))
  }
}
