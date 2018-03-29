package  klarna.fp

import org.scalatest._

import fs2.Stream


class StreamingSpec extends FlatSpec with Matchers {
  import Streaming._

  "A stream of a single element" should "only contain one element" in {
    singleElement(1).toList shouldEqual List(1)
  }

  "A stream of multiple elements" should "contain the specified elements" in {
    multipleElements(1, 2, 3).toList shouldEqual List(1, 2, 3)
  }

  "A stream from list" should "contain the correct elements" in {
    streamFromList(List(1, 2)).toList shouldEqual List(1, 2)
  }

  "A stream from lists" should "contain the correct elements in the correct order" in {
    streamFromLists(List(1, 2), List(3, 4), List(5, 6, 7)).toList shouldEqual List.range(1, 8)
  }

  "A concatenated stream" should "be correctly ordered" in {
    val s1 = Stream(1, 2)
    val s2 = Stream(3, 4)

    concatenate(s1, s2).toList shouldEqual List(1, 2, 3, 4)
  }

  "A stream of repeated values" should "contain as many values as taken out" in {
    repeated(1).take(10).toList shouldEqual List.fill(10)(1)
  }

  it should "only contain one value" in {
    val xs = repeated(1).take(12).toList
    assert(
      xs.forall(_ == 1),
      s"- expected only values of 1, but got list of: $xs")
  }

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

  "Two interleaved streams" should "have the correct length" in {
    val s1 = Stream(1, 3, 5)
    val s2 = Stream(2, 4, 6)

    interleave(s1, s2).toList.length shouldEqual List.range(1, 7).length
  }

  it should "contain the correct elements" in {
    val s1 = Stream(1, 3, 5)
    val s2 = Stream(2, 4, 6)

    interleave(s1, s2).toList shouldEqual List.range(1, 7)
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
    val s1 = unfold(0)(_ + 1).take(4).toList
    s1 shouldEqual List(0, 1, 2, 3)
  }

  "A flattened stream" should "be able to handle an empty list" in {
    flatten(Stream(List[Int]())).toList shouldEqual List[Int]()
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

  "intersperse" should "have every other element as supplied" in {
    val s1 = Stream(1, 2, 3)
    intersperse(s1, 0).toList shouldEqual List(1, 0, 2, 0, 3)
  }

  it should "work for streams with one element" in {
    val s1 = Stream(1)
    intersperse(s1, 0).toList shouldEqual List(1)
  }

  it should "work for streams with two elements" in {
    val s1 = Stream(1, 2)
    intersperse(s1, 0).toList shouldEqual List(1, 0, 2)
  }

}
