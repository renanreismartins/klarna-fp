package klarna.fp
package data

import scala.annotation.tailrec
import org.scalatest.{FlatSpec, Matchers}

class RandomSpec extends FlatSpec with Matchers {
  import Random._

  "Generator" should "give correct value for three seeds" in {
    val gen0 = Generator(0)

    val (gen1, rnd0) = gen0.nextInt
    rnd0 should be (0)

    val (gen2, rnd1) = gen1.nextInt
    rnd1 should be (0)

    val (gen3, rnd2) = gen2.nextInt
    rnd2 should be (4232237)
  }

  "nextInt" should
  "give back the correct state and values when queried three times" in {
    val rnds = for {
      _    <- nextInt
      _    <- nextInt
      rnd1 <- nextInt
    } yield rnd1

    rnds.run(0).value should be (2389171320405252413L, 4232237)
  }

  "nextBoolean" should "give back true if nextInt was > 0" in {
    def assumeCoherent(int: Int, bool: Boolean) =
      if (int > 0) assert(bool)
      else assert(!bool)

    @tailrec
    def assertSeed(times: Int, seed: Long): Unit = {
      val (newSeed, int)  = nextInt.run(seed).value
      val (_      , bool) = nextBoolean.run(seed).value
      assumeCoherent(int, bool)
      if (times > 0) assertSeed(times - 1, newSeed)
    }

    assertSeed(20, 0L)
  }

  "Rand" should "give the correct value for three seeds" in {
    val s = for {
      _  <- nextInt
      _  <- nextInt
      l1 <- nextInt
      b1 <- nextBoolean
    } yield (l1, b1)

    val (gen, tuple) = s.run(0).value
    tuple should be ((4232237, true))
  }

}
