package  klarna.fp

import org.scalatest._

import fs2.Stream
import Streams._


class StreamTests extends FlatSpec with Matchers {

  "An even stream" should "contain only even numbers" in {
    even {
      Stream.emits(List.range(1, 11))
    }
      .toList shouldEqual List(2, 4, 6, 8, 10)
  }

  "A sum of a stream" should "give back a single result" in {
    sum {
      Stream.emits(List.range(1, 11))
    }
      .toList.length shouldEqual 1
  }

  it should "return the correct sum" in {
    sum {
      Stream.emits(List.range(1, 11))
    }
      .toList shouldEqual List(List.range(1, 11).foldLeft(0)(_ + _))
  }

  "A combination of two streams" should "have the correct length" in {
    val s1 = Stream(1, 3, 5)
    val s2 = Stream(2, 4, 6)

    combine(s1, s2).toList.length shouldEqual List.range(1, 7).length
  }

  it should "contain the correct elements" in {
    val s1 = Stream(1, 3, 5)
    val s2 = Stream(2, 4, 6)

    combine(s1, s2).toList shouldEqual List.range(1, 7)
  }

  "A sum of two streams" should "have the correct length" in {
    val s1 = Stream(1, 3, 5)
    val s2 = Stream(2, 4, 6)

    sum(s1, s2).toList.length should be (1)
  }

  it should "have the correct value" in {
    val s1 = Stream(1, 3, 5)
    val s2 = Stream(2, 4, 6)

    sum(s1, s2).toList shouldEqual List(List.range(1, 7).sum)
  }

  "An unfolded Stream of incrementation" should "have the correct values" in {
    val s1 = unfold(0, _ + 1).take(3).toList
    s1 shouldEqual List(1, 2, 3)
  }

  "A flattened stream" should "be able to handle an empty list" in {
    flatten(Stream(List())).toList shouldEqual List()
  }

  it should "correctly flatten a stream of a single list" in {
    flatten {
      Stream(List(1, 2, 3))
    }
      .toList shouldEqual List(1, 2, 3)
  }

  it should "correctly flatten a stream of multiple lists" in {
    flatten {
      Stream(List.range(1, 4), List.range(4, 9))
    }
      .toList shouldEqual List.range(1, 9)
  }

}
