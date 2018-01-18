package klarna.fp
package basics

import org.scalatest._

class ExtractorSpec extends FlatSpec with Matchers {
  import Extractor._

  "Birthday.valid unapply" should "work for a valid birthday" in {
    "2018-01-01" match {
      case Birthday.valid(_) =>
        // success!
      case _ =>
        fail("unapply did not work as expected")
    }
  }

  it should "not work for an invalid birthday" in {
    "20180101" match {
      case Birthday.valid(bd) =>
        fail("Shouldn't have been a valid birthday for: 20180101")
      case _ =>
        // success!
    }
  }

  it should "not work for a birthday in the future" in {
    "21180101" match {
      case Birthday.valid(bd) =>
        fail("Shouldn't have been a valid birthday for: 21180101")
      case _ =>
        // success!
    }
  }

  "Birthday.invalid unapply" should "not work for a valid birthday" in {
    "2018-01-01" match {
      case Birthday.invalid(_) =>
        fail("unapply did not work as expected")
      case _ =>
        // success!
    }
  }

  it should "work for an invalid birthday" in {
    "20180101" match {
      case Birthday.invalid(bd) =>
        // success!
      case _ =>
        fail("Should have been an invalid birthday for: 20180101")
    }
  }

  it should "work for a birthday in the future" in {
    "21180101" match {
      case Birthday.invalid(bd) =>
        // success!
      case _ =>
        fail("Should have been an invalid birthday for: 21180101")
    }
  }

}
