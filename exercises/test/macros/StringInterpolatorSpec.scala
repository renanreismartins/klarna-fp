package klarna.fp
package macros

import org.scalatest.{FlatSpec, Matchers}
import StringInterpolator._

class InterpolatorSpec extends FlatSpec with Matchers {

  it should "create the correct string for a lg string without values interpolated" in {
    fail("You need to remove this line and uncomment the ones below!")
    //val noVals = lg"no vars!"
    //
    //assert(noVals === "no vars!")
  }

  it should "not compile when trying to interpolate values" in {
    fail("You need to remove this line and uncomment the ones below!")
    //val x = 1
    //
    //"""lg"some var $x"""" shouldNot compile
  }

}
