package klarna.fp

import java.io.{ ByteArrayOutputStream, PrintStream }

import org.scalatest._
import cats.implicits._

import FreeMonads._

class FreeMonadsTests extends FlatSpec with Matchers {

  val Store = StoreOps[StoreOp]

  "A store" should "be able to put values" in {
    Store.put("one", 1).foldMap(StoreId()) should be (())
  }

  it should "be able to get the same values back out" in {
    val program = for {
      _ <- Store.put("one", 1)
      v <- Store.get[Int]("one")
    } yield v

    program.foldMap(StoreId()) should be (Option(1))
  }

  it should "get None back when updating a non-existing value" in {
    val program = for {
      v <- Store.update[Int]("one", 1)
    } yield v

    program.foldMap(StoreId()) should be (None)
  }

  it should "get the old value back when updating an existing value" in {
    val program = for {
      _ <- Store.put("one", 1)
      v <- Store.update[Int]("one", 2)
    } yield v

    program.foldMap(StoreId()) should be (Option(1))
  }

  it should "be able to delete an inserted value" in {
    val program = for {
      _ <- Store.put("one", 1)
      b <- Store.delete("one")
      v <- Store.get[Int]("one")
    } yield (b, v)

    program.foldMap(StoreId()) should be ((true, None))
  }

  it should "not be able to delete value that doesn't exist" in {
    val program = for {
      b <- Store.delete("one")
      v <- Store.get[Int]("one")
    } yield (b, v)

    program.foldMap(StoreId()) should be ((false, None))
  }

  "A StoreLog" should "create a message for each operation" in {
    val os = new ByteArrayOutputStream()
    val ps = new PrintStream(os)

    val program = for {
      _ <- Store.put("one", 1)
      b <- Store.delete("one")
      v <- Store.get[Int]("one")
    } yield (b, v)

    program.foldMap(StoreLog(ps, StoreId())) should be ((true, None))
    os.toString("utf-8").lines.toList.length should be (3)
  }
}
