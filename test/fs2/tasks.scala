package klarna.fp

import org.scalatest._
import fs2.Task

class TasksTests extends FlatSpec with Matchers { self =>
  "`immediately`" should "evaluate side effects without being called" in {
    var gotCalled = false
    lazy val x = {
      gotCalled = true
      1
    }

    Tasks.immediately(x)
    assert(gotCalled, "side effects were not evaluated")
  }

  "`later`" should "not evaluate side effects without being called" in {
    var gotCalled = false
    lazy val x = {
      gotCalled = true
      1
    }

    Tasks.later(x)
    assert(!gotCalled, "side effects were evaluated")
  }

  it should "evaluate effects when called" in {
    var gotCalled = false
    lazy val x = {
      gotCalled = true
      1
    }

    Tasks.later(x).unsafeRun()
    assert(gotCalled, "side effects were not evaluated")
  }

  "`safeTask`" should "catch exceptions" in {
    Tasks.safeTask(
      Tasks.later(throw new Exception)).unsafeRun()
  }

  "`recover`" should "catch exceptions" in {
    val t =
      Tasks.recover(Tasks.later[Int](throw new Exception))(Tasks.later(1))

    t.unsafeRun() should be (1)
  }

  it should "not evaluate the first or second task" in {
    var didRun = false
    val t1 = Tasks.later[Int](throw new Exception)
    val t2 = Tasks.later {
      didRun = true
      1
    }

    Tasks.recover(t1)(t2)
    assert(!didRun, "did evaluate second task in `recover`")
  }

  "`ensure`" should "not evaluate task without it being run" in {
    Tasks.ensure(Tasks.later(1))(_ != 1, new Exception)
  }

  it should "should throw when the predicate is true" in {
    assertThrows[Exception] {
      Tasks.ensure(Tasks.later(1))(_ != 1, new Exception).unsafeRun()
    }
  }

  it should "should not throw when the predicate is false" in {
    Tasks.ensure(Tasks.later(1))(_ == 1, new Exception).unsafeRun()
  }

  "`product`" should "not execute the tasks first" in {
    var didRun = false
    val t1 = Tasks.later {
      didRun = true
      1
    }

    val t2 = Tasks.later {
      didRun = true
      "two"
    }

    Tasks.product(t1, t2)
    assert(!didRun, "product executed tasks before producing tuple")
  }

  "`traverse`" should "not execute immediately" in {
    Tasks.flatten {
      List(
        Tasks.later(1),
        Tasks.later(throw new Exception),
      )
    }
  }

  it should "not catch exceptions when run" in {
    assertThrows[Exception] {
      Tasks.flatten {
        List(
          Tasks.later(1),
          Tasks.later(throw new Exception),
        )
      }
      .unsafeRun()
    }
  }

  it should "evaluate tasks in parallel" in {
    var t1Done = false
    var t1Started = false
    var t2Started = false

    val t1 = Tasks.later {
      self.synchronized {
        t1Started = true
        notifyAll()
        self.wait(1000)
      }
      assert(t2Started,
        "threads didn't execute out of order, second thread not started")
      t1Done = true
    }
    val t2 = Tasks.later {
      self.synchronized {
        if (!t1Started) self.wait(10000)
        assert(t1Started,
          "threads didn't execute out of order, first thread not started")
        t2Started = true
        self.wait(2000)
      }
      assert(t1Done, "threads didn't execute out of order")
    }

    Tasks.flatten(List(t1, t2)).unsafeRun()
  }

  "`traverse`" should "produce the correct result" in {
    val xs = List(Tasks.later(1), Tasks.later(2), Tasks.later(3))

    Tasks.traverse(xs)(a => Tasks.later(a + 1))
      .unsafeRun() shouldEqual List(2, 3, 4)
  }

  it should "not evaluate tasks before being run" in {
    val xs = List(Tasks.later(1), Tasks.later(throw new Exception), Tasks.later(3))
    Tasks.traverse(xs)(a => Tasks.later(a + 1))
  }
}
