package klarna.fp
package basics

import cats.Id
import org.scalatest.{FlatSpec, Matchers}

class KleisliTriplesSpec extends FlatSpec with Matchers {
  import KleisliTriples._

  "PingEndpoint" should """respond to GET /ping with "pong"""" in {
    (for {
      pong <- PingEndpoint(Request("GET", "/ping"))
    } yield {
      pong should be (Some(Response(200, "pong")))
    }).unsafeRunSync()
  }

  it should "not respond on other endpoints or methods" in {
    (for {
      p1 <- PingEndpoint(Request("GET", "/pong"))
      p2 <- PingEndpoint(Request("POST", "/ping"))
      p3 <- PingEndpoint(Request("POST", "/pong"))
    } yield {
      p1 should be (None)
      p2 should be (None)
      p3 should be (None)
    }).unsafeRunSync()
  }

  "extractList" should "be able to extract integers from a request" in {
    val req = Request("GET", "/doublesum", "1, 2, 3, 4, 5, 6, 7, 8, 9, 10")
    extractList[Id].run(req) should be (List.range(1, 11))
  }

  "doubleElements" should "double all elements in a list" in {
    val xs = List(1, 2, 3)
    doubleElements[Id].run(xs) should be (List(2, 4, 6))
  }

  "foldElements" should "have the correct sum for non-empty list" in {
    val xs = List(1, 2, 3)
    foldElements[Id].run(xs) should be (Some(6))
  }

  it should "return None on empty list" in {
    foldElements[Id].run(List.empty) should be (None)
  }

  "sumToResponse" should "return the correct sum for a valid input" in {
    sumToResponse[Id].run(Some(Sum(1))) should be (Some(Response(200, "1")))
  }

  it should "return None for an invalid input" in {
    sumToResponse[Id].run(None) should be (Some(Response(400, badResponseBody)))
  }

  "sumPipeline" should "return the correct response" in {
    sumPipeline[Id].run(Request("GET", "/doublesum", "1, 2, 3")) should be (Some(Response(200, "6")))
  }

  "SumEndpoint" should "respond correctly on GET /doublesum" in {
    (for {
      s1 <- SumEndpoint(Request("GET", "/doublesum", "1, 2, 3"))
      s2 <- SumEndpoint(Request("GET", "/doublesum", ""))
    } yield {
      s1 should be (Some(Response(200, "6")))
      s2 should be (Some(Response(400, badResponseBody)))
    }).unsafeRunSync()
  }

  it should "respond correctly on invalid request" in {
    (for {
      s1 <- SumEndpoint(Request("GET", "/doesntExist", "1, 2, 3"))
      s2 <- SumEndpoint(Request("GET", "/doesntExist", ""))
      s3 <- SumEndpoint(Request("POST", "/doublesum", ""))
    } yield {
      s1 should be (None)
      s2 should be (None)
      s3 should be (None)
    }).unsafeRunSync()
  }
}
